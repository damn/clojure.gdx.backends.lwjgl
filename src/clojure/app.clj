(ns clojure.app)

(defprotocol Listener
  "An ApplicationListener is called when the Application is created, resumed, rendering, paused or destroyed. All methods are called in a thread that has the OpenGL context current. You can thus safely create and manipulate graphics resources.

  The ApplicationListener interface follows the standard Android activity life-cycle and is emulated on the desktop accordingly. "

  (create [_]
          "Called when the Application is first created.")

  (dispose [_]
           "Called when the Application is destroyed. Preceded by a call to pause().")

  (pause [_]
         "Called when the Application is paused, usually when it's not active or visible on-screen. An Application is also paused before it is destroyed.")

  (render [_]
          "Called when the Application should render itself.")

  (resize [_ width height]
          "Called when the Application is resized. This can happen at any point during a non-paused state but will never happen before a call to create().

          Parameters:

          * `width` - the new width in pixels

          * `height` - the new height in pixels ")

  (resume [_]
          "Called when the Application is resumed from a paused state, usually when it regains focus."))
