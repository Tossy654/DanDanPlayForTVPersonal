rootProject.name="DanDanPlayForTV"
include(":app",
        ":module-tv",
        ":module-torrent",
        ":module-player",
        ":common",

        ":libs:thunder",
        ":libs:file-picker",
        ":libs:tool-danma",
        ":libs:tool-subtitle",
        ":libs:tool-torrent")

include(":libs:tool-vlc")
include(":libs:tool-vlc:libvlc",
        ":libs:tool-vlc:extension-api",
        ":libs:tool-vlc:medialibrary")
include(":libs:tool-vlc:application:tools",
        ":libs:tool-vlc:application:resources",
        ":libs:tool-vlc:application:mediadb",
//        ":libs:tool-vlc:application:app",
        ":libs:tool-vlc:application:live-plot-graph",
        ":libs:tool-vlc:application:television")
include(":libs:tool-vlc:application:vlc-android")
include(":libs:tool-vlc:application:moviepedia")
