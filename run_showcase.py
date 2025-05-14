#!/usr/bin/env python3

import os
import time
import sys
import subprocess
import argparse
import json
from datetime import datetime
from config import AUTH_TOKEN, DEFAULT_TIMEOUT

def setup_args():
    parser = argparse.ArgumentParser(description='Run Band Manager showcase scenario')
    parser.add_argument('--users', type=int, default=25,
                        help='Number of concurrent users (default: 25)')
    parser.add_argument('--spawn-rate', type=int, default=5,
                        help='Users to spawn per second (default: 5)')
    parser.add_argument('--run-time', type=int, default=20, 
                        help='Duration of the test in seconds (default: 300)')
    parser.add_argument('--output', type=str, default='showcase_results', 
                        help='Output directory for results (default: showcase_results)')
    parser.add_argument('--services', type=str, default='all',
                        help='Services to test (all, bands, tours, albums, songs, offers)')
    return parser.parse_args()

def ensure_output_dir(output_dir):
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    return output_dir

def check_services_availability():
    import requests
    from requests.exceptions import RequestException
    
    base_url = "http://localhost"
    
    token = AUTH_TOKEN
    if token.startswith("Bearer "):
        token = token[7:]
    
    auth_headers = {
        "Authorization": f"Bearer {token}"
    }
    
    services = {
        "user-service": f"{base_url}:8091/api/artists",
        "band-management": f"{base_url}:8092/api/bands",
        "music-catalog": f"{base_url}:8093/api/songs",
        "tour-management": f"{base_url}:8094/api/tours",
        "auth-service": f"{base_url}:8084/health"
    }
    
    print("Checking services availability...")
    all_available = True

    for service_name, endpoint in services.items():
        try:
            headers = auth_headers
            
            timeout = DEFAULT_TIMEOUT
            
            print(f"Checking {service_name} at {endpoint} (timeout: {timeout}s)...")
            response = requests.get(endpoint, headers=headers, timeout=timeout)
            
            if service_name == "auth-service":
                if response.status_code < 500:
                    print(f"✅ {service_name} service is available")
                else:
                    print(f"❌ {service_name} service returned error: {response.status_code}")
                    all_available = False
            else:
                if response.status_code in [200, 204, 404]:
                    print(f"✅ {service_name} service is available")
                else:
                    print(f"⚠️ {service_name} service returned status code {response.status_code}")
                    print(f"   Response body: {response.text[:200]}..." if len(response.text) > 200 else f"   Response body: {response.text}")
                    all_available = False
        except RequestException as e:
            print(f"❌ {service_name} service is not available: {str(e)}")
            print(f"   Error type: {type(e).__name__}")
            if hasattr(e, 'response') and e.response is not None:
                print(f"   Response status: {e.response.status_code}")
                print(f"   Response body: {e.response.text[:200]}..." if len(e.response.text) > 200 else f"   Response body: {e.response.text}")
            all_available = False
    
    return all_available

def run_locust_test(users, spawn_rate, run_time, output_dir, services):
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    
    html_report = os.path.join(output_dir, f"report_{timestamp}.html")
    
    tags = []
    if services != 'all':
        for service in services.split(','):
            tags.append(f"--tags {service.strip()}")
    
    command = [
        "locust",
        "-f", "locustfile.py",
        "--headless",
        "--host", "http://localhost",
        "--users", str(users),
        "--spawn-rate", str(spawn_rate),
        "--run-time", f"{run_time}s",
        "--html", html_report
    ]
    
    if tags:
        command.extend(tags)
    
    print(f"Starting showcase scenario with {users} users...")
    print(f"Command: {' '.join(command)}")
    
    try:
        process = subprocess.Popen(
            command,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            universal_newlines=True
        )
        
        for line in process.stdout:
            sys.stdout.write(line)
        
        process.wait()
        if process.returncode != 0:
            print(f"Locust process exited with code {process.returncode}")
            return False
            
    except KeyboardInterrupt:
        print("\nTest interrupted by user.")
        process.terminate()
        return False
    except Exception as e:
        print(f"Error running Locust: {e}")
        return False
        
    return True

def analyze_results(output_dir, run_time):
    stats_files = [f for f in os.listdir(output_dir) if f.startswith('stats_') and f.endswith('.csv')]
    if not stats_files:
        print("No results files found.")
        return
        
    latest_stats = max(stats_files)
    stats_path = os.path.join(output_dir, latest_stats)
    
    summary = {
        "endpoints": {},
        "total_requests": 0,
        "failed_requests": 0,
        "avg_response_time": 0,
        "median_response_time": 0,
        "95th_percentile": 0,
        "services": {
            "band_management": {"requests": 0, "failures": 0, "avg_time": 0},
            "tour_management": {"requests": 0, "failures": 0, "avg_time": 0},
            "music_catalog": {"requests": 0, "failures": 0, "avg_time": 0},
            "user_service": {"requests": 0, "failures": 0, "avg_time": 0}
        }
    }
    
    service_mapping = {
        "/api/bands": "band_management",
        "/api/bands/offers": "band_management",
        "/api/tours": "tour_management",
        "/api/cityVisits": "tour_management",
        "/api/albums": "music_catalog",
        "/api/songs": "music_catalog",
        "/api/artists": "user_service",
        "/api/managers": "user_service",
        "/api/link": "user_service",
        "/api/unlink": "user_service"
    }
    
    with open(stats_path, 'r') as f:
        lines = f.readlines()
        header = lines[0].strip().split(',')
        
        type_idx = header.index("Type")
        name_idx = header.index("Name")
        requests_idx = header.index("# requests")
        failures_idx = header.index("# failures")
        median_idx = header.index("Median response time")
        avg_idx = header.index("Average response time")
        p95_idx = header.index("95%")
        
        total_row = None
        
        for line in lines[1:]:
            parts = line.strip().split(',')
            if len(parts) < len(header):
                continue
                
            if parts[type_idx] == "None" and parts[name_idx] == "Aggregated":
                total_row = parts
                continue
                
            if parts[type_idx] != "None":
                endpoint = parts[name_idx]
                requests = int(parts[requests_idx])
                failures = int(parts[failures_idx])
                median = float(parts[median_idx])
                avg = float(parts[avg_idx])
                p95 = float(parts[p95_idx])
                
                summary["endpoints"][endpoint] = {
                    "requests": requests,
                    "failures": failures,
                    "median_response_time": median,
                    "avg_response_time": avg,
                    "95th_percentile": p95
                }
                
                for prefix, service in service_mapping.items():
                    if prefix in endpoint:
                        summary["services"][service]["requests"] += requests
                        summary["services"][service]["failures"] += failures
                        current_total = summary["services"][service]["avg_time"] * (summary["services"][service]["requests"] - requests)
                        new_total = current_total + (avg * requests)
                        summary["services"][service]["avg_time"] = new_total / summary["services"][service]["requests"] if summary["services"][service]["requests"] > 0 else 0
                        break
    
    if total_row:
        summary["total_requests"] = int(total_row[requests_idx])
        summary["failed_requests"] = int(total_row[failures_idx])
        summary["median_response_time"] = float(total_row[median_idx])
        summary["avg_response_time"] = float(total_row[avg_idx])
        summary["95th_percentile"] = float(total_row[p95_idx])
    
    print("\n" + "=" * 70)
    print("BAND MANAGER MICROSERVICES - FESTIVAL SEASON RUSH RESULTS")
    print("=" * 70)
    print(f"Total Requests: {summary['total_requests']}")
    failure_rate = (summary['failed_requests'] / summary['total_requests'] * 100) if summary['total_requests'] > 0 else 0
    print(f"Failed Requests: {summary['failed_requests']} ({failure_rate:.2f}%)")
    print(f"Average Response Time: {summary['avg_response_time']:.2f} ms")
    print(f"Median Response Time: {summary['median_response_time']:.2f} ms")
    print(f"95th Percentile: {summary['95th_percentile']:.2f} ms")
    
    print("\nPer-Service Performance:")
    print("-" * 70)
    for service_name, metrics in summary["services"].items():
        if metrics["requests"] > 0:
            service_failure_rate = (metrics["failures"] / metrics["requests"] * 100) if metrics["requests"] > 0 else 0
            print(f"{service_name.replace('_', ' ').title()}:")
            print(f"  Requests: {metrics['requests']}")
            print(f"  Failed: {metrics['failures']} ({service_failure_rate:.2f}%)")
            print(f"  Avg Response: {metrics['avg_time']:.2f} ms")
            print("")
    
    thresholds = {
        "failure_rate": 1.0,
        "avg_response_time": 500.0,  
        "95th_percentile": 1000.0,  
        "band_management_rps": 100.0,
        "tour_management_rps": 50.0, 
        "music_catalog_rps": 30.0,   
        "user_service_rps": 40.0     
    }
    
    test_duration = summary["total_requests"] / (summary["total_requests"] / run_time) if summary["total_requests"] > 0 else run_time
    for service, metrics in summary["services"].items():
        if metrics["requests"] > 0:
            metrics["rps"] = metrics["requests"] / test_duration
    
    success = (
        failure_rate <= thresholds["failure_rate"] and
        summary["avg_response_time"] <= thresholds["avg_response_time"] and
        summary["95th_percentile"] <= thresholds["95th_percentile"]
    )
    
    print("\nTop 10 Slowest Endpoints:")
    print("-" * 70)
    sorted_endpoints = sorted(
        summary["endpoints"].items(), 
        key=lambda x: x[1]["avg_response_time"], 
        reverse=True
    )
    for endpoint, stats in sorted_endpoints[:10]:
        endpoint_failure_rate = (stats['failures'] / stats['requests'] * 100) if stats['requests'] > 0 else 0
        print(f"{endpoint}:")
        print(f"  Requests: {stats['requests']}")
        print(f"  Failures: {stats['failures']} ({endpoint_failure_rate:.2f}%)")
        print(f"  Avg Response: {stats['avg_response_time']:.2f} ms")
        print(f"  95%: {stats['95th_percentile']:.2f} ms")
        print("")
    
    print("=" * 70)
    if success:
        print("✅ SHOWCASE SUCCESSFUL - All performance targets met!")
    else:
        print("❌ SHOWCASE NEEDS IMPROVEMENT - Some performance targets were not met.")
    print("=" * 70)
    
    print("\nPerformance Insights:")
    print("-" * 70)
    
    for service, metrics in summary["services"].items():
        if metrics["requests"] > 0:
            threshold_key = f"{service}_rps"
            if threshold_key in thresholds:
                rps = metrics["rps"]
                target = thresholds[threshold_key]
                status = "✅" if rps >= target else "❌"
                service_name = service.replace("_", " ").title()
                print(f"{status} {service_name}: {rps:.2f} req/sec (Target: {target} req/sec)")
    
    status = "✅" if summary["avg_response_time"] <= thresholds["avg_response_time"] else "❌"
    print(f"{status} Average Response Time: {summary['avg_response_time']:.2f} ms (Target: {thresholds['avg_response_time']} ms)")
    
    status = "✅" if summary["95th_percentile"] <= thresholds["95th_percentile"] else "❌"
    print(f"{status} 95th Percentile: {summary['95th_percentile']:.2f} ms (Target: {thresholds['95th_percentile']} ms)")
    
    status = "✅" if failure_rate <= thresholds["failure_rate"] else "❌"
    print(f"{status} Failure Rate: {failure_rate:.2f}% (Target: <= {thresholds['failure_rate']}%)")
    
    summary_file = os.path.join(output_dir, "latest_summary.json")
    with open(summary_file, 'w') as f:
        json.dump(summary, f, indent=2)
    
    print(f"\nDetailed HTML report saved to: {output_dir}/report_*.html")
    print(f"Summary data saved to: {summary_file}")

def main():
    args = setup_args()
    output_dir = ensure_output_dir(args.output)
    
    print("=" * 70)
    print("BAND MANAGER SHOWCASE: FESTIVAL SEASON RUSH")
    print("=" * 70)
    print(f"Users: {args.users}")
    print(f"Spawn Rate: {args.spawn_rate} users/second")
    print(f"Duration: {args.run_time} seconds")
    print(f"Services to test: {args.services}")
    print("=" * 70)
    
    if not check_services_availability():
        print("\n⚠️ Warning: Some services may not be available. Continue anyway? (y/n)")
        if input().lower() != 'y':
            print("Showcase cancelled by user.")
            return
    
    success = run_locust_test(
        args.users, 
        args.spawn_rate, 
        args.run_time, 
        output_dir,
        args.services
    )
    
    if success:
        time.sleep(1)
        analyze_results(output_dir, args.run_time)

if __name__ == "__main__":
    main()