import { useState, useEffect } from "react";

/**
 * Hook que evalúa una media query y devuelve true cuando coincide.
 * Útil para detectar viewport mobile (ej: max-width: 768px).
 */
export function useMediaQuery(query: string): boolean {
  const [matches, setMatches] = useState(() => {
    if (typeof window === "undefined") return false;
    return window.matchMedia(query).matches;
  });

  useEffect(() => {
    const media = window.matchMedia(query);
    const handler = (e: MediaQueryListEvent) => setMatches(e.matches);
    media.addEventListener("change", handler);
    setMatches(media.matches);
    return () => media.removeEventListener("change", handler);
  }, [query]);

  return matches;
}
