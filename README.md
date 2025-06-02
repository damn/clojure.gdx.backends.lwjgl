# API docs:

https://damn.github.io/clojure.gdx.backends.lwjgl/

# 0. Do not set! global state, pass to create...

-> option in Gdx not to pass global state? pass Gdx context as an object?

-> what won't work ? SpriteBatch ?

# 1. Do I have exposed all the API or am I hiding the library
;= > look javadoc

# 2. TODOS

* TODO: add set glfw-async or xStartOnFirstThread (see libgdx wiki, lwjgl glfw lib )
    and hint those functions initialising GLFW (monitors, display-modes) ...

* TODO what does it bring: openal, opengl, etc. ... ?
    mp3s, OGG, wav
    multi-window ?
    cursor
    files
    net
    graphics emulation ANGLE (extra lib has to get )
    -> check all libraries depened on and list them here ( with icon ? & documentation ? )
    e.g. OpenAL docs wiki link or website link

* what does this lib depend on (natives -? which ones alwyas? )

* config options ?

* forgot cursor API

* main loop synced ...

* tests - from gdx ?

* test win/linux

* rewrite yourself its nothing -> can configure easier with load-deps ... even a visual tool

* how to setup a hello world example -> example repo?
