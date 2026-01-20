# **Listening DNA**

Music is universal, but the way we listen is deep. Instead of letting some algorithms tell me what my year sounded like, I’m digging into the raw history myself. The goal is to walk through every single stream and decode my own habits because it’s fun.

---

## **The Data**

The project picks apart the **Spotify Extended Streaming History**, which is basically a play-by-play of every track since the account started.

* **Temporal Mapping**: Every stream is pinned to a `ts` timestamp in UTC, so I can rebuild my entire listening timeline exactly how it happened.


* **Vibe Checks**: The `skipped` field tells the truth about what I actually bailed on, regardless of what's saved in my library.


* **Intent Analysis**: Looking at `reason_start` shows if I was actually hunting for a specific song or just letting the autoplay "trackdone" logic take the wheel.


* **Volume of Life**: Tracking `ms_played` to see how much of my life was actually spent inside certain tracks.


* **Safe Space Indicator**: Digging into the `incognito_mode` flag to find the stuff I played when I didn't want Spotify’s social features watching.


---

## **The Technical Approach**

To keep this creative and fast, I’m sticking to a stack that doesn't feel like "work":

* **Scala 3**: Using the clean syntax and the type system to model the domain without the usual mess.

---

## **How it Works**

No idea, it is just a beginning.

Will see :) 