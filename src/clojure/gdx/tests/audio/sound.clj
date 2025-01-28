(ns clojure.gdx.tests.audio.sound
  (:require [clojure.gdx.application :as application]
            [clojure.gdx.backends.lwjgl :as lwjgl])
  (:import [com.badlogic.gdx Gdx]
           [com.badlogic.gdx.audio Sound]
           [com.badlogic.gdx.graphics GL20]
           [com.badlogic.gdx.scenes.scene2d Stage]
           [com.badlogic.gdx.scenes.scene2d.ui Skin Label SelectBox Slider TextButton Table]
           [com.badlogic.gdx.scenes.scene2d.utils ClickListener ChangeListener]
           [com.badlogic.gdx.utils Align]
           [com.badlogic.gdx.utils.viewport FitViewport]))

(def file-names ["shotgun.ogg",
                 "shotgun-8bit.wav",
                 "shotgun-32float.wav",
                 "shotgun-64float.wav",
                 "quadraphonic.ogg",
                 "quadraphonic.wav",
                 "bubblepop.ogg",
                 "bubblepop-stereo-left-only.wav"])

(def current-sound (atom nil))

#_(defn set-sound [file-name]
  (when-let [sound @current-sound]
    (.dispose sound))
  (reset! current-sound (audio/sound (fh/child (files/internal gdx/*files* "data") file-name))))

; lein run -m clojure.dev-loop "(do (require 'clojure.gdx.tests.audio.sound) (clojure.gdx.tests.audio.sound/-main))"
(defn -main []
  (.set org.lwjgl.system.Configuration/GLFW_LIBRARY_NAME "glfw_async")
  (lwjgl/application (reify application/Listener
                       (create [_]
                         #_{:skin (scene2d.ui/skin (files/internal "data/uiskin.json"))
                          :ui (scene2d/stage (viewport/fit 640 400))
                          :sound-selector (doto (SelectBox. skin)
                                            (.setItems file-names))
                          }
                         #_(set-sound (.getSelected sound-selector))
                         )

                       (dispose [_])

                       (pause [_])

                       (render [_])

                       (resize [_ width height])

                       (resume [_]))))
