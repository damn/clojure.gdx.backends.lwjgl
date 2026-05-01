(defproject clojure.gdx.backends.lwjgl "-SNAPSHOT"
  :dependencies [
                 [com.badlogicgames.gdx/gdx-backend-lwjgl3    "1.14.0"]
                 [com.badlogicgames.gdx/gdx-platform          "1.14.0" :classifier "natives-desktop"]
                 [org.clojure/clojure "1.12.0"]
                 ]
  :source-paths ["src"]
  :java-source-paths ["java-src"]
  :aliases {"dev"      ["run" "-m" "moon.dev-loop" "((requiring-resolve 'moon.start/-main))"]}
  :plugins [[lein-hiera "2.0.0"]
            [lein-codox "0.10.8"]]
  :codox {:source-uri "https://github.com/damn/moon/blob/main/{filepath}#L{line}"
          :metadata {:doc/format :markdown}}
  :global-vars {*warn-on-reflection* true
                ;*unchecked-math* :warn-on-boxed
                ;*assert* false
                *print-level* 3})
