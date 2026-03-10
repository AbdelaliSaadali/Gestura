<div align="center">
  <img src="/Logo.jpeg" alt="Gestura Logo" width="120" height="120" />

  # 🤟 Gestura

  **AI-Powered Bidirectional Sign Language Translation & Interactive E-Learning Platform**

  [![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat&logo=android&logoColor=white)](https://www.android.com/)
  [![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
  [![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat&logo=jetpack-compose&logoColor=white)](https://developer.android.com/jetpack/compose)
  [![Python](https://img.shields.io/badge/Backend-Python-3776AB?style=flat&logo=python&logoColor=white)](https://www.python.org/)
  [![AI](https://img.shields.io/badge/AI-MediaPipe-0082FB?style=flat&logo=google&logoColor=white)](https://mediapipe.dev/)
</div>

<br />

## 📖 Overview

**Gestura** is an innovative accessibility platform designed to bridge the communication gap between deaf and hearing individuals. Developed as an academic Innovation Project (PI), it leverages real-time computer vision, artificial intelligence, and a distributed cloud architecture to deliver seamless, two-way translation and interactive sign language education.

To ensure a smooth user experience on mobile devices, the Android application acts as a lightweight, highly accessible client, while the heavy machine learning inference is offloaded to a scalable cloud backend.

---

## ✨ Key Features

### 🔄 Bidirectional Translation
* **Sign-to-Text / Speech:** Utilizes the smartphone camera to track hand gestures and spatial movements in real-time, translating sign language into readable text and synthesized audio via a frosted-glass overlay UI.
* **Voice-to-Sign:** Captures spoken language (Speech-to-Text) and translates it into sign language, displayed through a responsive 3D avatar.

### 🎓 Interactive Learning Portal
* **Real-time AI Feedback:** An integrated E-learning module that allows users to learn basic sign language. The system uses computer vision to draw bounding boxes around the user's hands, validating gesture accuracy in real-time.
* **Gamification:** Tracks user progress, daily streaks, and "words learned" to encourage consistent practice.

---

## 🛠️ Technical Architecture & Stack

The system is built with a focus on high performance, leveraging cloud computing to handle intensive Deep Learning tasks without draining the mobile device's battery or processing power.

### **📱 Mobile Client (Frontend)**
* **Framework:** Android (Native)
* **UI Toolkit:** Jetpack Compose (Kotlin) with Material Design 3
* **Hardware Integration:** CameraX API for live feed processing

### **☁️ Cloud Backend & AI (Infrastructure)**
* **API:** Python (FastAPI / Flask) for high-performance, asynchronous endpoints.
* **Computer Vision:** Google MediaPipe (Pose and Hand landmark tracking) and custom Deep Learning models for sequence classification.
* **Infrastructure:** AWS (Amazon Web Services) for scalable ML inference instances.
* **Database:** 
  * **MongoDB:** To store user profiles, learning progress, and custom dictionaries.
  * **Redis:** For low-latency caching of user streaks and active session data.

---

## 🚀 Getting Started

Follow these steps to set up the project locally for development and testing.

### Prerequisites
* **Android Studio:** Ladybug or later recommended
* **Android SDK:** Version 35
* **Backend:** Python 3.10+ (if running your own backend services)

### Installation (Mobile Client)

1. **Clone the repository:**
   ```bash
   git clone https://github.com/AbdelaliSaadali/Gestura.git
   cd Gestura
   ```

2. **Open the project in Android Studio:**
   * Launch Android Studio.
   * Select **Open an existing Android Studio project**.
   * Navigate to the cloned `Gestura` folder and open it.

3. **Sync dependencies:**
   * Allow Gradle to sync and download all necessary project dependencies.

4. **Run the app:**
   * Connect an Android device with camera support or use an emulator with a configured webcam.
   * Click the **Run** ▶️ button in Android Studio.

---

## 📸 Screenshots

*(Replace with actual screenshots of your Compose UI)*

|<img src="demo/stitch_sign_to_text_translation (2)/screen.png" width="200" alt="Sign to Text Translation Screen">|<img src="demo/stitch_sign_to_text_translation (3)/screen.png" width="200" alt="Learning Portal Screen">|
|:---:|:---:|
| **Real-time Translation** | **Interactive Learning** |

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.