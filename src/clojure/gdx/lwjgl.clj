(ns clojure.gdx.lwjgl
  (:require [clojure.java.io :as io])
  (:import (com.badlogic.gdx ApplicationAdapter)
           (com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application Lwjgl3ApplicationConfiguration)
           (com.badlogic.gdx.utils SharedLibraryLoader)
           (java.awt Taskbar Toolkit)
           (org.lwjgl.system Configuration)))

(defn- set-taskbar-icon [icon]
  (.setIconImage (Taskbar/getTaskbar)
                 (.getImage (Toolkit/getDefaultToolkit)
                            (io/resource icon))))

(def ^:private mac? SharedLibraryLoader/isMac)

(defn- configure-glfw-for-mac []
  (.set Configuration/GLFW_LIBRARY_NAME "glfw_async")
  (.set Configuration/GLFW_CHECK_THREAD0 false))

(defn- ->config [{:keys [title width height fps]}]
  (doto (Lwjgl3ApplicationConfiguration.)
    (.setTitle title)
    (.setWindowedMode width height)
    (.setForegroundFPS fps)))

(defprotocol Application
  (create  [_])
  (dispose [_])
  (render  [_])
  (resize  [_ w h]))

(defn- gdx-application [listener]
  (proxy [ApplicationAdapter] []
    (create []
      (create listener))
    (dispose []
      (dispose listener))
    (render []
      (render listener))
    (resize [w h]
      (resize listener w h))))

(defn start [{:keys [taskbar-icon] :as config} application]
  (when taskbar-icon
    (set-taskbar-icon taskbar-icon))
  (when mac?
    (configure-glfw-for-mac))
  (Lwjgl3Application. (gdx-application application)
                      (->config config)))
