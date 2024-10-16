# Java Video Converter

## Overview

The Java Video Converter is a desktop application that allows users to convert video files from MKV and WEBM formats to MP4. It offers a user-friendly interface built with JavaFX, enabling users to select files or directories for conversion and specify the quality of the output video using the Constant Rate Factor (CRF).

## Features

- Convert individual MKV and WEBM files to MP4.
- Convert all files in a selected directory.
- Optionally, include all subdirectories for conversion.
- User-friendly GUI with progress indication.
- Customizable CRF value for output video quality.

## Technologies Used

- Java 23
- JavaFX for GUI
- JavaCV and FFmpeg for video processing
- Maven for project management and dependencies

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed on your machine:

1. **Java Development Kit (JDK)**: Make sure you have JDK 11 or higher installed.

2. **Apache Maven**: Install Maven to manage the project and its dependencies. You can download it from [Maven's official website](https://maven.apache.org/download.cgi).

### Cloning the Repository

1. Open your terminal and run the following command to clone the repository:

   ```
   bash
   git clone https://github.com/Patrick-B4/java-video-converter.git
   ```
2. Navigate into the project directory:
    ```
    cd java-video-converter
    ```
### Building the Project
    
    mvn clean install
    
### Running the Application
    
    mvn javafx:run
    
## Usage Instructions
1. When the application starts, select a file or directory using the provided buttons.
2. Enter the desired CRF value for video quality (default is 17, lower values indicate higher quality).
3. Click on the "Start Conversion" button to begin the conversion process.
4. The application will display a progress bar indicating the conversion status.
5. Once the conversion is complete, you will receive a success message.

### Notes
This project uses an FFMPEG wrapper, therefore you do not need to install FFMPEG before using the program.
