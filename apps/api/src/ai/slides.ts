/** One slide of a lesson material. number 0 = the text has no "Diapositiva N." markers (the whole text is one unit). */
export type Slide = { number: number; text: string };

/**
 * Splits extracted text into slides. The material format is one paragraph per slide, starting with
 * "Diapositiva N." (blank lines between paragraphs). Pure: no I/O.
 */
export function parseSlides(text: string): Slide[] {
  const slides: Slide[] = [];
  for (const paragraph of text.split(/\n\s*\n/).map((p) => p.trim()).filter(Boolean)) {
    const header = /^Diapositiva\s+(\d+)\.\s*([\s\S]*)$/i.exec(paragraph);
    if (header) slides.push({ number: Number(header[1]), text: header[2].trim() });
    else if (slides.length) slides[slides.length - 1].text += `\n${paragraph}`;
  }
  return slides.length ? slides : [{ number: 0, text: text.trim() }];
}
