<div align="center">
  <img src="/Logo.jpeg" alt="Gestura Logo" width="120" height="120" />

  # 🤟 Gestura

  **AI-Powered Bidirectional Sign Language Translation & Interactive E-Learning Platform**

  [![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat&logo=android&logoColor=white)](https://www.android.com/)
  [![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
  [![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat&logo=jetpack-compose&logoColor=white)](https://developer.android.com/jetpack/compose)
  [![Python](https://img.shields.io/badge/Backend-Python-3776AB?style=flat&logo=python&logoColor=white)](https://www.python.org/)
  [![FastAPI](https://img.shields.io/badge/API-FastAPI-009688?style=flat&logo=fastapi&logoColor=white)](https://fastapi.tiangolo.com/)
  [![AI](https://img.shields.io/badge/AI-MediaPipe-0082FB?style=flat&logo=google&logoColor=white)](https://mediapipe.dev/)

  > ⚠️ **Academic Project** — Developed as an Innovation Project (PI) at BDCC S4.

</div>

<br />

---

## 📖 Overview

**Gestura** is an innovative accessibility platform designed to bridge the communication gap between deaf and hearing individuals. It leverages real-time computer vision, artificial intelligence, and a distributed backend architecture to deliver seamless two-way translation and interactive sign language education.

The Android application acts as a lightweight, highly accessible client, while heavy machine learning inference is offloaded to a scalable backend — keeping the app fast and battery-friendly on mobile.

---

## ✨ Key Features

### 🔄 Bidirectional Translation
- **Sign-to-Text:** Uses the smartphone camera to track hand gestures and spatial movements in real-time, translating sign language into readable text via a frosted-glass overlay UI.
- **Voice-to-Sign:** Captures spoken English and translates it into ASL, displayed as a smooth stick figure animation driven by real MediaPipe skeleton keyframes.

### 🎓 Interactive Learning Portal
- **Real-time AI Feedback:** An integrated e-learning module that lets users practice sign language. Computer vision draws bounding boxes around the user's hands and validates gesture accuracy live.
- **Gamification:** Tracks progress, daily streaks, and words learned to encourage consistent practice.

---

## 🎤 Voice to Sign — How It Works

```
🎤 Speak  →  📱 SpeechRecognizer  →  🌐 POST /translate  →  🦴 Skeleton Frames  →  🖥️ Canvas Animation
```

1. User taps the mic button and speaks naturally in English
2. Android's native **SpeechRecognizer** converts speech to text on-device
3. Text is sent to the **FastAPI backend** via `POST /translate`
4. Backend **normalizes** the text — lowercases, expands contractions, removes ASL stop words (`the`, `a`, `is`, `are`, `was`, `to`, etc.)
5. Remaining words are mapped to **ASL glosses** (e.g. "eating" → `EAT`)
6. Each gloss is looked up in **`asl_landmarks.json`** — 364 ASL words pre-encoded as MediaPipe skeleton keyframes
7. Android receives the frames and **animates a stick figure** on a Compose `Canvas` at 80ms per frame
8. Words not found in the vocabulary are **fingerspelled** letter by letter as a fallback

> 💡 No video files. No downloads. The entire animation runs from ~10MB of JSON keyframe data — fully offline after first load.

---

## 🛠️ Technical Architecture & Stack

### 📱 Mobile Client (Android)
| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material Design 3 |
| Navigation | Compose Navigation |
| Networking | Retrofit 2 + Gson |
| Speech | Android SpeechRecognizer (native) |
| Animation | Compose Canvas — MediaPipe skeleton rendering |
| Camera | CameraX API |

### ☁️ Backend & AI
| Layer | Technology |
|---|---|
| API Framework | Python FastAPI + Uvicorn |
| ASL Dataset | MuteMotion (WLASL → MediaPipe landmarks, 364 words) |
| Landmark Format | MediaPipe — 180 landmarks × (x, y, z) per frame |
| Animation Data | `asl_landmarks.json` (~10MB, fully offline) |
| Computer Vision | Google MediaPipe (Pose + Hand tracking) |
| Database | MongoDB (user profiles, learning progress) |
| Cache | Redis (streaks, active sessions) |
| Infrastructure | AWS (scalable ML inference) |

---

## 🌐 API Reference

Base URL (local): `http://10.0.2.2:8000` (emulator) or `http://localhost:8000`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/translate` | Translate English text → ASL skeleton frames |
| `GET` | `/health` | Server status + number of words loaded |
| `GET` | `/vocabulary` | List all 364 available ASL glosses |
| `GET` | `/lookup/{word}` | Get skeleton frames for a single word |

**Example request:**
```bash
curl -X POST http://localhost:8000/translate \
  -H "Content-Type: application/json" \
  -d '{"text": "eat coffee church"}'
```

**Example response:**
```json
{
  "success": true,
  "glosses": ["EAT", "COFFEE", "CHURCH"],
  "signs": [
    {
      "gloss": "EAT",
      "type": "skeleton",
      "found": true,
      "frames": [[[0.5, 0.3, 0.0], "...180 landmarks per frame..."]]
    },
    {
      "gloss": "HELLO",
      "type": "fingerspell",
      "found": false,
      "frames": []
    }
  ]
}
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Ladybug or later
- **Android SDK** version 35
- **Python** 3.10+ (for backend)

### 1. Clone the repository
```bash
git clone https://github.com/AbdelaliSaadali/Gestura.git
cd Gestura
```

### 2. Run the backend
```bash
cd backend
pip install -r requirements.txt
python3 -m uvicorn main:app --host 0.0.0.0 --port 8000
```

Verify it's running:
```bash
curl http://localhost:8000/health
# {"status": "ok", "words_loaded": 364}
```

### 3. Run the Android app
- Open the `Gestura` folder in **Android Studio**
- Let Gradle sync and download dependencies
- Connect a physical Android device (recommended) or configure an emulator
- Click **Run ▶️**

> 📌 The app connects to the backend at `http://10.0.2.2:8000` — this is the standard emulator address for localhost. On a real device on the same network, replace with your Mac's local IP address.

---

## 📁 Project Structure

```
GESTURA/
├── app/                        ← Android Kotlin app (Jetpack Compose)
│   └── src/main/
│       ├── screens/
│       │   ├── VoiceToSignScreen.kt    ← mic + canvas animation
│       │   ├── VoiceToSignViewModel.kt ← SpeechRecognizer + Retrofit
│       │   └── HomeScreen.kt
│       └── data/
│           ├── api/GesturaApi.kt       ← Retrofit interface
│           └── model/TranslateModels.kt
├── backend/
│   ├── main.py                 ← FastAPI server
│   ├── asl_landmarks.json      ← 364 ASL words × MediaPipe keyframes
│   ├── requirements.txt
│   └── archive/                ← raw MuteMotion dataset (npz + json)
├── demo/                       ← screenshots
└── README.md
```

---

## 📸 Screenshots

| | | | |
|:---:|:---:|:---:|:---:|
|<img src="demo/stitch_sign_to_text_translation (2)/screen.png" width="180" alt="Real-time Translation">|<img src="demo/stitch_sign_to_text_translation (3)/screen.png" width="180" alt="Interactive Learning">|<img src="demo/stitch_sign_to_text_translation (4)/screen.png" width="180" alt="Voice to Sign">|<img src="demo/stitch_sign_to_text_translation (5)/screen.png" width="180" alt="Fingerspelling">|
| **Real-time Translation** | **Interactive Learning** | **Voice to Sign** | **Fingerspelling** |

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 👥 Team

> This project was developed as an **Innovation Project (PI) at BDCC S4**.

| Contributor | Responsibility |
|---|---|
| **Abdelali Saadali** | Voice-to-Sign feature — Android UI (Jetpack Compose screens, Canvas skeleton animation, SpeechRecognizer wiring) + FastAPI backend (MuteMotion dataset integration, MediaPipe keyframe pipeline, REST API) |

---

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.