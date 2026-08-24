# Fix Frontend Errors & Test Endpoints

## Errors Identified

1. **Duplicate key `digital-carbon-footprint`** — The `/impacts` API returns a `DIGITAL_CARBON_FOOTPRINT` impact whose category maps to the same id as the hardcoded `DigitalCarbonSection` stop in `page.tsx`, causing duplicate React keys.

2. **`useSession` must be wrapped in `<SessionProvider />`** — `DigitalCarbonAction.tsx` uses `next-auth/react`'s `useSession`, but `layout.tsx` doesn't provide a `SessionProvider`.

## Steps

- [x] Create TODO.md with plan
- [x] Fix duplicate key in `page.tsx` by filtering out the digital-carbon impact from the impacts list
- [x] Create a `Providers` client component wrapping `SessionProvider`
- [x] Wrap `layout.tsx` children with `Providers`
- [ ] Run the frontend dev server and verify no errors
- [ ] Test backend endpoints (/api/impacts, /api/digital-carbon/benchmarks, /api/digital-carbon/estimate, hope-ledger, confessions, etc.)
