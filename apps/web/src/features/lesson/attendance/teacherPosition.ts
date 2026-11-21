/**
 * Where the teacher is, for the centre of the allowed area. null when the browser has no geolocation, the teacher said
 * no, or it took too long: the round then opens WITHOUT location and every scan lands "por revisar".
 */
export function teacherPosition(timeoutMs = 8000): Promise<{ lat: number; lng: number } | null> {
  return new Promise((resolve) => {
    if (!("geolocation" in navigator)) return resolve(null);
    navigator.geolocation.getCurrentPosition(
      (p) => resolve({ lat: p.coords.latitude, lng: p.coords.longitude }),
      () => resolve(null),
      { enableHighAccuracy: true, timeout: timeoutMs, maximumAge: 10_000 },
    );
  });
}
