# Fitness Project - AI-Powered Fitness Tracking System

A microservices-based fitness tracking application built with Spring Boot. This system allows users to register, track their fitness activities, and receive AI-generated, personalized recommendations and safety guidelines based on their workout metrics using the Gemini AI API.

## 🏗️ Architecture Overview

The system is designed using a microservices architecture and consists of the following services:

1. **Eureka Discovery Server (`eureka`) - Port: 8761**
   - Acts as the service registry for all microservices to locate and communicate with each other.
   
2. **User Service (`userService`) - Port: 8091**
   - Manages user registration and profile retrieval.
   - Validates user existence for other services.
   - **Database**: PostgreSQL (`fitness_user_db`)

3. **Activity Service (`activityService`) - Port: 8092**
   - Tracks and records fitness activities (Running, Cycling, Yoga, etc.).
   - Communicates synchronously via `WebClient` with the User Service to validate users.
   - Publishes activity events asynchronously to RabbitMQ for AI processing.
   - **Database**: MongoDB (`activity`)

4. **AI Service (`aiService`) - Port: 8093**
   - Consumes messages from RabbitMQ when a new activity is logged.
   - Calls the Google Gemini AI API to generate detailed workout analysis, improvement suggestions, and safety guidelines.
   - Stores and retrieves generated recommendations.
   - **Database**: MongoDB (`aidatabase`)

*Note: The services also include configurations (`bootstrap.yml`) expecting a Spring Cloud Config Server on port `8888`.*

## 🚀 Technologies Used

* **Language**: Java 17 / 21 / 23 (varies by service)
* **Framework**: Spring Boot, Spring Cloud (Eureka, Config)
* **Databases**: PostgreSQL (User data), MongoDB (Activities and AI Recommendations)
* **Message Broker**: RabbitMQ
* **AI Integration**: Google Gemini API via Spring WebFlux (`WebClient`)
* **Build Tool**: Maven
* **Boilerplate Reduction**: Lombok

## 🛠️ Prerequisites

Before running the project, ensure you have the following installed and running:

* **Java Development Kit (JDK)**
* **PostgreSQL** (Running on `localhost:5432`)
* **MongoDB** (Running on `localhost:27017`)
* **RabbitMQ** (Running on `localhost:5672` with default `guest:guest` credentials)

## ⚙️ Configuration & Setup

### 1. Database Setup
Create the required PostgreSQL database for the User Service:

    CREATE DATABASE fitness_user_db;

*(MongoDB databases `activity` and `aidatabase` will be created automatically upon first insertion).*

### 2. Environment Variables
The AI Service requires a Gemini API key to function. Set the following environment variables (or configure them in your `aiService/src/main/resources/application.yml`):

    export GEMINI_API_URL="https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" 
    export GEMINI_API_KEY="your_google_gemini_api_key_here"

### 3. Build the Project
Navigate to each service directory and build the projects using Maven:

    cd eureka && ./mvnw clean install
    cd ../userService && ./mvnw clean install
    cd ../activityService && ./mvnw clean install
    cd ../aiService && ./mvnw clean install

## 🏃‍♂️ Running the Application

To ensure proper communication, start the services in the following order:

1. **Start Eureka Server** (Wait for it to initialize completely on port 8761)
2. **Start User Service** (Port 8091)
3. **Start Activity Service** (Port 8092)
4. **Start AI Service** (Port 8093)

You can run each service using the Spring Boot Maven plugin from their respective directories:

    ./mvnw spring-boot:run

Once started, you can view the Eureka Dashboard at `http://localhost:8761`.

## 📡 API Endpoints

### User Service (`http://localhost:8091`)
* `POST /api/users/register` - Register a new user.
* `GET /api/users/{userId}` - Get user profile details.
* `GET /api/users/{userId}/validate` - Validate if a user exists (Internal use).

### Activity Service (`http://localhost:8092`)
* `POST /api/activities` - Log a new activity (Triggers AI analysis).
* `GET /api/activities` - Get activities for a user (Requires `X-USER-ID` header).
* `GET /api/activities/{activityId}` - Get a specific activity.

### AI Service (`http://localhost:8093`)
* `GET /api/recommendations/user/{userId}` - Get all AI recommendations for a specific user.
* `GET /api/recommendations/activity/{activityId}` - Get the AI recommendation for a specific activity.

## 🧵 Asynchronous Flow Details
When an activity is created via the Activity Service:
1. The activity is saved to MongoDB.
2. An event containing the activity data is published to RabbitMQ (`fitness.exchange` with routing key `activity.tracking`).
3. The `ActivityMessageListener` in the AI Service consumes this event.
4. The AI Service generates a specialized prompt including the workout metrics and sends it to the Gemini API.
5. The parsed JSON recommendation (Analysis, Improvements, Suggestions, Safety) is stored in the AI Service's MongoDB.
