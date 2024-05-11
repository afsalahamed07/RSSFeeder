# RSS Feeder Application

## Overview

This RSS Feeder Application is a backend service designed using Spring Boot. It incorporates the ROME library to fetch and process RSS feeds efficiently, providing users with regularly updated content from various sources in a streamlined format.

## Features

- **Fetch RSS Feeds:** Automatically retrieves RSS feed data from specified URLs to keep the content fresh and up-to-date.
- **Process Feeds:** Leverages the ROME library to parse the incoming feeds and format them into a user-friendly structure.
- **Cache Management:** Uses Redis to cache feed data, which enhances the response times and reduces the frequency of API calls, ensuring efficient performance even under load.

## Prerequisites

Before setting up the project, ensure you have the following installed:
- **Java JDK 11** or higher – Required to run the Spring Boot application.
- **Maven 3.6** or higher – Necessary for building the application.
- **Redis server** – Used for caching purposes to optimize performance.

## Project Status

This project is currently in an early stage of development and includes some basic functionalities. There is substantial work to be done to enhance its capabilities and performance. Future updates will focus on expanding features, improving user interface, and refining the back-end processes.

## Getting Started

To get the application up and running on your local machine for development and testing purposes, follow these steps:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourgithubusername/rss-feeder.git
   cd rss-feeder
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   java -jar target/rssfeeder-0.0.1-SNAPSHOT.jar
   ```

4. **Access the application:**
   Open your browser and navigate to `http://localhost:8080` to view and manage the RSS feeds.
