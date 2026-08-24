# Earth Crying — Project Brief (Implemented State)

## Core idea
Most climate content repeats the same five facts (CO2, deforestation, plastic straws). "Earth Crying" hits harder because it exposes damage people don't know they're causing — every screen answers "wait, THAT is hurting the planet?" — then immediately pairs it with hope: what's being done, what you can do, and proof that recovery is possible.

Structure: **Wound → Why it's invisible → Real-time proof (data) → Hope → Your action.**

---

## 1. The "Unnoticed Damage" categories (content backbone)

Each of these becomes a themed section/chapter in the site, and a data entity in the backend.

| # | Hidden Damage | Why people miss it |
|---|---|---|
| 1 | **Digital/AI carbon footprint** — streaming, cloud storage, data centers, AI model training/inference | Feels "virtual," has no physical trace |
| 2 | **Tire & brake dust** — non-exhaust vehicle emissions, worse than tailpipe particulates in some cities, including from EVs | People assume EVs = zero pollution |
| 3 | **Fast fashion microplastics** — synthetic fibers shed in every wash, entering oceans | Clothes look clean and solid, not like "waste" |
| 4 | **Light pollution** — disrupts bird migration, insect populations, coral spawning | Seen as safety/aesthetic, not ecological harm |
| 5 | **Underwater noise pollution** — shipping/sonar disorienting whales and fish | Ocean is "silent" to us |
| 6 | **Synthetic fertilizer runoff** — dead zones in oceans, soil microbiome collapse | Farming is seen as "natural" |
| 7 | **E-waste & rare-earth mining** — every phone upgrade drives toxic mining | Old phone just "sits in a drawer," feels harmless |
| 8 | **Hidden water footprint** — a single cotton T-shirt ≈ 2,700 liters of water; a burger ≈ 2,400 liters | Water use isn't visible on the product |
| 9 | **Palm oil supply chains** — hidden in ~50% of packaged goods, driving deforestation | Ingredient lists obscure it |
| 10 | **Space debris & satellite pollution** — atmospheric metal pollution from mega-constellations | Feels irrelevant to daily life |
| 11 | **Soil erosion from monoculture** — topsoil loss threatens future food security | Invisible underground process |
| 12 | **Indoor VOCs/fragrance chemicals** — air fresheners, cleaning products polluting indoor + outdoor air | "Clean smell" = perceived as safe |

Each category = one **content entity** with: hidden stat, source citation, live/estimated counter, "hope" story, and 3 concrete actions.

---

## 2. Backend features (Spring Boot)

### A. Content & Data Engine
- **Impact Entity CRUD API** — each "unnoticed damage" topic as a manageable entity (title, hidden-stat, explanation, sources, video asset ref, solutions[], hope-story). Powers CMS-style admin so you can add new topics without redeploying.
- **"Hope Ledger" API** — positive counterpart to every wound: reforestation progress, renewable capacity added, species recovered, policy wins — pulled from real APIs on a schedule, cached, served fast.
- **Solutions/Actions API** — each impact links to ranked, doable actions (individual, community, policy-level), each taggable by effort level (1-min / weekly habit / lifestyle change).

### B. Live/Real-Time Data Layer
- **External data aggregator (Spring Scheduler + WebClient)** — cron jobs pulling from NASA, NOAA, Global Forest Watch, OpenAQ, World Bank APIs; normalized and cached in Postgres/Redis so the frontend never waits on 3rd-party latency.
- **"Earth Pulse" WebSocket feed** — push live incrementing counters (est. trees lost, e-waste generated, microfibers shed) computed from known global per-second rates — this is what makes the homepage feel alive.
- **Geo Impact API** — country/region-level dataset (deforestation rate, AQI, water stress) for an interactive globe/map.

### C. User Engagement Layer
- **Personal "Hidden Footprint" Calculator** — quiz-style API scoring a user's lifestyle against the 12 categories, returning a personalized breakdown + top 3 highest-impact fixes.
- **Pledge & Streak Tracker** — users commit to actions, track streaks, unlock badges (gamified habit change, not guilt-based).
- **Community Solutions Wall** — users submit local actions/orgs; upvote + moderation queue.
- **"Ask Earth" AI companion** — a conversational endpoint (Claude/LLM-backed) that answers "is X actually bad for the planet?" in real time, citing the impact database.
- **Simulation Engine** — "If 1 million people did X" calculator, showing collective-impact projections to counter helplessness.

### D. Infra & Delivery
- **Video/Media CMS** — manage hero videos, transition clips, category videos; served via CDN (Cloudinary/Mux) with signed URLs.
- **Auth (JWT via Spring Security)** — for pledges, community posts, saved footprint history.
- **Newsletter/digest service** — periodic "state of the planet" email using aggregated Hope Ledger + Pulse data.
- **Notification scheduler** — Earth Day, World Environment Day, and personalized streak reminders.

---

## 3. Frontend concept (the "masterpiece" layer)

- **Scrollytelling hero**: full-bleed video that morphs from a thriving ecosystem into a damaged one as the user scrolls, driven by GSAP ScrollTrigger + Lenis/Locomotive smooth scroll.
- **3D data globe** (Three.js / React Three Fiber) showing live Earth Pulse counters and Geo Impact data as glowing hotspots.
- **Split-screen "Wound vs Hope"** transitions between sections — every damage screen has a matching hope screen, connected by a single continuous animation.
- **Custom cursor + micro-interactions**, sound design toggle (ambient nature vs. city noise), dark "eco" theme.
- **Animated data counters** (Framer Motion / GSAP) for footprint stats and hope-ledger numbers.
- **Reduced-motion & mobile fallback** paths for accessibility and performance.

---

## 4. Suggested stack

- **Backend**: Spring Boot 3, Spring Data JPA + PostgreSQL, Spring Security (JWT), Spring Scheduler, Redis (cache), WebSocket (STOMP) for Earth Pulse, deployed on Railway.
- **Frontend**: Next.js/React, Tailwind, GSAP + ScrollTrigger, Three.js/R3F, Framer Motion, Lenis smooth scroll, video via Mux/Cloudinary.
- **External data**: NASA APIs, Global Forest Watch, OpenAQ, NOAA, World Bank Open Data.

---

## 5. Ready-to-use prompt for your AI coding agent

Copy this as the starting prompt once you've scaffolded the repo:

```
Build a full-stack web app called "Earth Crying" — an awareness platform that exposes
overlooked human activities damaging the environment, paired with hope and actionable
solutions for each. This is a frontend-first, Awwwards-tier visual experience backed
by a Spring Boot API.

BACKEND (Spring Boot 3, Java, PostgreSQL, Redis, WebSocket):
1. Impact entity CRUD (title, hiddenStat, explanation, sources[], videoAssetUrl,
   solutions[], hopeStory) with categories: digital carbon footprint, tire/brake dust,
   fast fashion microplastics, light pollution, underwater noise pollution, fertilizer
   runoff, e-waste/rare-earth mining, hidden water footprint, palm oil supply chains,
   space debris, soil erosion, indoor VOCs.
2. Hope Ledger entity + scheduled job that fetches/caches real data from public APIs
   (Global Forest Watch, NOAA, OpenAQ, World Bank) — normalize into a single response.
3. WebSocket "Earth Pulse" feed pushing live incrementing counters computed from known
   global per-second rates (e.g., est. trees lost/sec, e-waste generated/sec).
4. Personal footprint quiz API: scores a user against the 12 categories, returns
   ranked top-3 fixes.
5. Pledge/streak tracker with badges, JWT auth via Spring Security.
6. Community solutions wall (submit + upvote + moderation flag).
7. Geo impact API serving country-level stats for a map/globe visualization.
8. REST + WebSocket endpoints documented with OpenAPI/Swagger.

FRONTEND (Next.js, Tailwind, GSAP + ScrollTrigger, Three.js/React Three Fiber,
Framer Motion, Lenis smooth scroll):
1. Scrollytelling homepage: hero video morphs from thriving to damaged ecosystem as
   the user scrolls.
2. 3D interactive globe visualizing live Earth Pulse counters and geo impact hotspots.
3. One dedicated section per "hidden damage" category, each following:
   Wound → Why it's invisible → Live data proof → Hope story → 3 actions you can take.
4. Personal footprint quiz UI with animated results breakdown.
5. Dark eco-themed design system, custom cursor, ambient sound toggle, reduced-motion
   fallback, full mobile responsiveness.

Set up the repo structure, database schema/migrations, and a working homepage with
one fully-built category section end-to-end (data → API → animated frontend) as the
first milestone before scaling to the remaining categories.
```

You can hand this straight to your coding agent (or split it into backend-first / frontend-first sub-prompts) and iterate section by section from there.
