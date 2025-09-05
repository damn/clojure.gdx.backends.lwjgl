(ns clojure.gdx.backends.lwjgl.application.configuration.gl-emulation
  (:import (com.badlogic.gdx.backends.lwjgl3 Lwjgl3ApplicationConfiguration$GLEmulation)))

(defn k->value [gl-version]
  (case gl-version
    :gl-emulation/angle-gles20 Lwjgl3ApplicationConfiguration$GLEmulation/ANGLE_GLES20
    :gl-emulation/gl20         Lwjgl3ApplicationConfiguration$GLEmulation/GL20
    :gl-emulation/gl30         Lwjgl3ApplicationConfiguration$GLEmulation/GL30
    :gl-emulation/gl31         Lwjgl3ApplicationConfiguration$GLEmulation/GL31
    :gl-emulation/gl32         Lwjgl3ApplicationConfiguration$GLEmulation/GL32))
