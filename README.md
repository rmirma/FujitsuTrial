# Trial Application for Java Developer Internship at Fujitsu Estonia

## Description
The task was to develop a sub-functionality of the food delivery application. The application calculates the delivery fee for food couriers based on regional base fee, vehicle type, and weather conditions.

## Technologies
- Java 11
- Spring Boot
- H2 Database

## Installation
1. Clone the repository
2. Open the project in your IDE
3. Run the application
4. Use the following endpoints to test the application:
    - `GET /api/delivery-fee?vehicleType=<INSERT>&city=<INSERT>` 
        - example: `localhost:8080/delivery-fee?vehicleType=BIKE&city=TARTU`
        - Acceptable vehicleType parameters: CAR, BIKE, SCOOTER
        - Acceptable city parameters: TALLINN, TARTU, PARNU
        - The response will be a JSON object with the delivery fee
5. We also can request the contents of the database by using the following endpoint:
    - `GET /api/weather-data`
    - The response will be a JSON object with the weather data
