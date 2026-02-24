Place the following Lexend TTF font files in this directory:

- lexend_light.ttf
- lexend_regular.ttf
- lexend_medium.ttf
- lexend_semibold.ttf
- lexend_bold.ttf

Download from: https://fonts.google.com/specimen/Lexend

Note: File names must be lowercase, use underscores (no hyphens),
and match the names referenced in Type.kt.

# 🤟 Gestura

> **AI-Powered Bidirectional Sign Language Translation & Interactive E-Learning Platform**

![Gestura App UI](link-to-your-ui-screenshot.png) *(Note: Add a screenshot of your Compose UI here)*

## 📖 Overview
**Gestura** is an innovative accessibility platform designed to bridge the communication gap between deaf and hearing individuals. Developed as an academic Innovation Project (PI), it leverages real-time computer vision, artificial intelligence, and a distributed cloud architecture to deliver seamless, two-way translation and interactive sign language education.

To ensure a smooth user experience on mobile devices, the Android application acts as a lightweight, highly accessible client, while the heavy machine learning inference is offloaded to a scalable cloud backend.

## ✨ Key Features

### 🔄 Bidirectional Translation
* **Sign-to-Text / Speech:** Utilizes the smartphone camera to track hand gestures and spatial movements in real-time, translating sign language into readable text and synthesized audio via a frosted-glass overlay UI.
* **Voice-to-Sign:** Captures spoken language (Speech-to-Text) and translates it into sign language, displayed through a responsive 3D avatar.

### 🎓 Interactive Learning Portal
* **Real-time AI Feedback:** An integrated E-learning module that allows users to learn basic sign language. The system uses computer vision to draw bounding boxes around the user's hands, validating gesture accuracy in real-time.
* **Gamification:** Tracks user progress, daily streaks, and "words learned" to encourage consistent practice.

## 🛠️ Technical Architecture & Stack

The system is built with a focus on high performance, leveraging cloud computing to handle intensive Deep Learning tasks without draining the mobile device's battery or processing power.

**📱 Mobile Client (Frontend)**
* **Framework:** Android (Native)
* **UI Toolkit:** Jetpack Compose (Kotlin) with Material Design 3
* **Hardware Integration:** CameraX API for live feed processing

**☁️ Cloud Backend & AI (Infrastructure)**
* **API:** Python (FastAPI / Flask) for high-performance, asynchronous endpoints.
* **Computer Vision:** Google MediaPipe (Pose and Hand landmark tracking) and custom Deep Learning models for sequence classification.
* **Infrastructure:** AWS (Amazon Web Services) for scalable ML inference instances.
* **Database:** * **MongoDB:** To store user profiles, learning progress, and custom dictionaries.
  * **Redis:** For low-latency caching of user streaks and active session data.

## 🚀 Getting Started

### Prerequisites
* Android Studio (Ladybug or later recommended)
* Android SDK 35
* Python 3.10+ (for backend services)

### Installation (Mobile Client)
1. Clone the repository:
   ```bash
   git clone [https://github.com/your-username/gestura.git](https://github.com/your-username/gestura.git)