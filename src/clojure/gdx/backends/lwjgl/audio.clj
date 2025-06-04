(ns clojure.gdx.backends.lwjgl.audio
  (:import (com.badlogic.gdx.backends.lwjgl3.audio OpenALLwjgl3Audio)
           (com.badlogic.gdx.backends.lwjgl3.audio.mock MockAudio)))

(defn mock []
  (MockAudio.))

(defn create [config]
  (OpenALLwjgl3Audio. (.audioDeviceSimultaneousSources config)
                      (.audioDeviceBufferCount         config)
                      (.audioDeviceBufferSize          config)))
