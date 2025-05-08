#!/usr/bin/env python3
"""
Band Manager Application - Showcase Runner
This script runs the "Festival Season Rush" scenario without requiring the Locust web interface.
It is customized for the microservices architecture of the Band Manager application.
"""

import os
import time
import sys
import subprocess
import argparse
import json
from datetime import datetime

def setup_args():
    """Set up command line arguments"""
    parser = argparse.ArgumentParser(description='Run Band Manager showcase scenario')
    parser.add_argument('--users', type=int, default=50, 
                        help='Number of concurrent users (default: 50)')
    parser.add_argument('--spawn-rate', type=int, default=10, 
                        help='Users to spawn per second (default: 10)')
    parser.add_argument('--run-time', type=int, default=300, 
                        help='Duration of the test in seconds (default: 300)')
    parser.add_argument('--host', type=str, default='http://localhost:8080', 
                        help='Host to load test (default: http://localhost:8080)')
    parser.add_argument('--output', type=str, default='showcase_results', 
                        help='Output directory for results (default: showcase_results)')
    parser.add_argument('--services', type=str, default='all',
                        help='Services to test (all, bands, tours, albums, songs, offers)')
    return parser.parse_args()

def ensure_output_dir(output_dir):
    """Ensure the output directory exists"""
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    return output_dir

def check_services_availability(host):
    """Check if the required microservices are available"""
    import requests
    from requests.exceptions import RequestException
    
    # Parse the base host without port if it has one
    base_url = host.split('://')[0] + '://' + host.split('://')[1].split(':')[0] if '://' in host else host.split(':')[0]
    
    services = {
        "user-service": f"{base_url}:8091/api/artists",
        "band-management": f"{base_url}:8092/api/bands",
        "music-catalog": f"{base_url}:8093/api/albums",
        "tour-management": f"{base_url}:8094/api/tours"
    }
    
    print("Checking services availability...")
    all_available = True
    
    for service_name, endpoint in services.items():
        try:
            response = requests.get(endpoint, timeout=5)
            if response.status_code in [200, 404]:  # 404 is ok too, might mean empty data
                print(f"✅ {service_name} service is available")
            else:
                print(f"⚠️ {service_name} service returned status code {response.status_code}")
                all_available = False
        except RequestException as e:
            print(f"❌ {service_name} service is not available: {str(e)}")
            all_available = False
    
    return all_available

def run_locust_test(users, spawn_rate, run_time, host, output_dir, services):
    """Run the Locust test headlessly and save results"""
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    
    # Create output filenames
    csv_prefix = os.path.join(output_dir, f"stats_{timestamp}")
    html_report = os.path.join(output_dir, f"report_{timestamp}.html")
    
    # Add custom tags for service filtering if not 'all'
    tags = []
    if services != 'all':
        for service in services.split(','):
            tags.append(f"--tags {service.strip()}")
    
    # Build the command
    command = [
        "locust",
        "-f", "locustfile.py",
        "--headless",
        "--users", str(users),
        "--spawn-rate", str(spawn_rate),
        "--run-time", f"{run_time}s",
        "--host", host,
        "--csv", csv_prefix,
        "--html", html_report
    ]
    
    # Add tags if specified
    if tags:
        command.extend(tags)
    
    print(f"Starting showcase scenario with {users} users...")
    print(f"Command: {' '.join(command)}")
    
    try:
        # Run the process
        process = subprocess.Popen(
            command,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            universal_newlines=True
        )
        
        # Print output in real-time
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
    """Analyze the results and provide a summary"""
    # Find the latest stats CSV file
    stats_files = [f for f in os.listdir(output_dir) if f.startswith('stats_') and f.endswith('.csv')]
    if not stats_files:
        print("No results files found.")
        return
        
    latest_stats = max(stats_files)
    stats_path = os.path.join(output_dir, latest_stats)
    
    # Read the stats file
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
    
    # Service endpoint mapping for analysis
    service_mapping = {
        "/api/bands": "band_management",
        "/api/bands/offers": "band_management",
        "/api/tours": "tour_management",
        "/api/cityVisits": "tour_management",
        "/api/albums": "music_catalog",
        "/api/songs": "music_catalog",
        "/api/artists": "user_service",
        "/api/managers": "user_service"
    }
    
    # Simple CSV parsing
    with open(stats_path, 'r') as f:
        lines = f.readlines()
        header = lines[0].strip().split(',')
        
        # Index the columns
        type_idx = header.index("Type")
        name_idx = header.index("Name")
        requests_idx = header.index("# requests")
        failures_idx = header.index("# failures")
        median_idx = header.index("Median response time")
        avg_idx = header.index("Average response time")
        p95_idx = header.index("95%")
        
        total_row = None
        
        # Process each row
        for line in lines[1:]:
            parts = line.strip().split(',')
            if len(parts) < len(header):
                continue
                
            if parts[type_idx] == "None" and parts[name_idx] == "Aggregated":
                total_row = parts
                continue
                
            if parts[type_idx] != "None":
                # Process individual endpoints
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
                
                # Map endpoint to service for service-level metrics
                for prefix, service in service_mapping.items():
                    if prefix in endpoint:
                        summary["services"][service]["requests"] += requests
                        summary["services"][service]["failures"] += failures
                        # Weighted average time calculation
                        current_total = summary["services"][service]["avg_time"] * (summary["services"][service]["requests"] - requests)
                        new_total = current_total + (avg * requests)
                        summary["services"][service]["avg_time"] = new_total / summary["services"][service]["requests"] if summary["services"][service]["requests"] > 0 else 0
                        break
    
    # Process totals
    if total_row:
        summary["total_requests"] = int(total_row[requests_idx])
        summary["failed_requests"] = int(total_row[failures_idx])
        summary["median_response_time"] = float(total_row[median_idx])
        summary["avg_response_time"] = float(total_row[avg_idx])
        summary["95th_percentile"] = float(total_row[p95_idx])
    
    # Print summary
    print("\n" + "=" * 70)
    print("BAND MANAGER MICROSERVICES - FESTIVAL SEASON RUSH RESULTS")
    print("=" * 70)
    print(f"Total Requests: {summary['total_requests']}")
    failure_rate = (summary['failed_requests'] / summary['total_requests'] * 100) if summary['total_requests'] > 0 else 0
    print(f"Failed Requests: {summary['failed_requests']} ({failure_rate:.2f}%)")
    print(f"Average Response Time: {summary['avg_response_time']:.2f} ms")
    print(f"Median Response Time: {summary['median_response_time']:.2f} ms")
    print(f"95th Percentile: {summary['95th_percentile']:.2f} ms")
    
    # Service-specific metrics
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
    
    # Define success thresholds
    thresholds = {
        "failure_rate": 1.0,  # 1% max failure rate
        "avg_response_time": 500.0,  # 500ms max average response time
        "95th_percentile": 1000.0,  # 1000ms max 95th percentile
        "band_management_rps": 100.0,  # Band operations per second
        "tour_management_rps": 50.0,  # Tour operations per second
        "music_catalog_rps": 30.0,     # Album/song operations per second
        "user_service_rps": 40.0       # User operations per second
    }
    
    # Calculate requests per second for each service
    test_duration = summary["total_requests"] / (summary["total_requests"] / run_time) if summary["total_requests"] > 0 else run_time
    for service, metrics in summary["services"].items():
        if metrics["requests"] > 0:
            metrics["rps"] = metrics["requests"] / test_duration
    
    # Check if the showcase was successful based on thresholds
    success = (
        failure_rate <= thresholds["failure_rate"] and
        summary["avg_response_time"] <= thresholds["avg_response_time"] and
        summary["95th_percentile"] <= thresholds["95th_percentile"]
    )
    
    # Print top 10 slowest endpoints
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
    
    # Detailed performance insights
    print("\nPerformance Insights:")
    print("-" * 70)
    
    # Service RPS check
    for service, metrics in summary["services"].items():
        if metrics["requests"] > 0:
            threshold_key = f"{service}_rps"
            if threshold_key in thresholds:
                rps = metrics["rps"]
                target = thresholds[threshold_key]
                status = "✅" if rps >= target else "❌"
                service_name = service.replace("_", " ").title()
                print(f"{status} {service_name}: {rps:.2f} req/sec (Target: {target} req/sec)")
    
    # Response time check
    status = "✅" if summary["avg_response_time"] <= thresholds["avg_response_time"] else "❌"
    print(f"{status} Average Response Time: {summary['avg_response_time']:.2f} ms (Target: {thresholds['avg_response_time']} ms)")
    
    # 95th percentile check
    status = "✅" if summary["95th_percentile"] <= thresholds["95th_percentile"] else "❌"
    print(f"{status} 95th Percentile: {summary['95th_percentile']:.2f} ms (Target: {thresholds['95th_percentile']} ms)")
    
    # Failure rate check
    status = "✅" if failure_rate <= thresholds["failure_rate"] else "❌"
    print(f"{status} Failure Rate: {failure_rate:.2f}% (Target: <= {thresholds['failure_rate']}%)")
    
    # Save summary to JSON for potential further analysis
    summary_file = os.path.join(output_dir, "latest_summary.json")
    with open(summary_file, 'w') as f:
        json.dump(summary, f, indent=2)
    
    print(f"\nDetailed HTML report saved to: {output_dir}/report_*.html")
    print(f"Summary data saved to: {summary_file}")

def main():
    """Main function to run the showcase"""
    args = setup_args()
    output_dir = ensure_output_dir(args.output)
    
    print("=" * 70)
    print("BAND MANAGER SHOWCASE: FESTIVAL SEASON RUSH")
    print("=" * 70)
    print(f"Target API: {args.host}")
    print(f"Users: {args.users}")
    print(f"Spawn Rate: {args.spawn_rate} users/second")
    print(f"Duration: {args.run_time} seconds")
    print(f"Services to test: {args.services}")
    print("=" * 70)
    
    # Check services availability
    if not check_services_availability(args.host):
        print("\n⚠️ Warning: Some services may not be available. Continue anyway? (y/n)")
        if input().lower() != 'y':
            print("Showcase cancelled by user.")
            return
    
    # Run the test
    success = run_locust_test(
        args.users, 
        args.spawn_rate, 
        args.run_time, 
        args.host, 
        output_dir,
        args.services
    )
    
    # Analyze results if the test ran successfully
    if success:
        time.sleep(1)  # Give a moment for files to be fully written
        analyze_results(output_dir)

if __name__ == "__main__":
    main()