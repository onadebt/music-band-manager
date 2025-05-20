# Band Manager - Festival season rush

## Overview

The "Festival Season Rush" scenario simulates peak-time activity in the Band Manager, replicating the intense workload experienced during festival season when the platform's various services face high concurrent usage. This load test is designed to validate the microservices architecture's resilience and performance under heavy load conditions.

## Scenario Description

During festival season, the Band Manager experiences a surge of activity as:

- Managers rush to create and update bands
- Artists receive and respond to multiple band join offers
- Tour managers frantically schedule multi-city tours
- Music catalog entries spike with new albums and songs
- User profiles undergo frequent updates

This scenario simulates all these activities occurring simultaneously with multiple concurrent users to stress test the system's capabilities.

## Microservices Under Test

The scenario tests the following microservices, which run on fixed ports:

1. **User Service** (port 8091): Manages artist and manager accounts
2. **Band Management Service** (port 8092): Handles band creation, updates, and member offers
3. **Music Catalog Service** (port 8093): Manages albums and songs
4. **Tour Management Service** (port 8094): Handles tour scheduling and city visits

## Performance Goals

The "Festival Season Rush" scenario aims to verify that the system can handle the following performance targets:

- **Failure Rate**: ≤ 1% across all services
- **Average Response Time**: ≤ 500ms
- **95th Percentile Response Time**: ≤ 1000ms

Each service has specific throughput targets:
- **Band Management**: 100+ requests per second
- **Tour Management**: 50+ requests per second
- **Music Catalog**: 30+ requests per second
- **User Service**: 40+ requests per second

## Expected Results

A successful test should demonstrate that:

1. All services remain responsive under load with minimal failures
2. Response times stay within defined thresholds
3. The system can handle the required throughput for each service
4. No bottlenecks emerge that would impact user experience during peak times
5. Resource utilization (CPU, memory, network) remains within acceptable limits

## Running the Test
With the services running, you need to obtain a token from
http://localhost:8084/token

Then you need to paste this token in the AUTH_TOKEN field in the config.py file. After that you can run the test with the following command:

python run_showcase.py --users 50 --spawn-rate 10 --run-time 300

You can customize the test parameters:
- `--users`: Number of concurrent virtual users (default: 50)
- `--spawn-rate`: Users to spawn per second (default: 10)
- `--run-time`: Duration of the test in seconds (default: 20)
- `--output`: Output directory for results (default: showcase_results)

## Interpreting Results

After the test completes, a comprehensive HTML report will be generated in the output directory. This report provides detailed metrics on:

- Overall system performance
- Per-service performance metrics
- Endpoint response times and failure rates
- Top 10 slowest endpoints

A successful test will show a "SHOWCASE SUCCESSFUL" message, indicating all performance targets were met.