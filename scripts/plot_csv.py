import csv
import math
import sys
from pathlib import Path


def parse_value(raw: str) -> float:
    value = raw.strip().lower()
    if value in {"nan", "inf", "+inf", "-inf"}:
        return math.nan
    return float(raw)


def read_csv(csv_path: Path):
    header = csv_path.read_text(encoding="utf-8").splitlines()[0]
    delimiter = ';' if ';' in header else ','

    xs = []
    ys = []
    with csv_path.open("r", encoding="utf-8") as f:
        reader = csv.DictReader(f, delimiter=delimiter)
        for row in reader:
            xs.append(float(row["X"]))
            ys.append(parse_value(row["Result"]))
    return xs, ys


def split_segments(xs, ys):
    segments = []
    current = []
    for x, y in zip(xs, ys):
        if math.isnan(y):
            if len(current) > 1:
                segments.append(current)
            current = []
            continue
        current.append((x, y))
    if len(current) > 1:
        segments.append(current)
    return segments


def map_value(value, src_min, src_max, dst_min, dst_max):
    if abs(src_max - src_min) < 1e-12:
        return (dst_min + dst_max) / 2.0
    ratio = (value - src_min) / (src_max - src_min)
    return dst_min + ratio * (dst_max - dst_min)


def plot_file(csv_path: Path, output_dir: Path):
    xs, ys = read_csv(csv_path)
    finite_y = [y for y in ys if not math.isnan(y)]
    if not finite_y:
        finite_y = [0.0]

    width = 1200
    height = 700
    margin_left = 90
    margin_right = 40
    margin_top = 60
    margin_bottom = 80

    x_min = min(xs)
    x_max = max(xs)
    y_min = min(finite_y)
    y_max = max(finite_y)
    if abs(y_max - y_min) < 1e-12:
        y_min -= 1.0
        y_max += 1.0

    plot_left = margin_left
    plot_right = width - margin_right
    plot_top = margin_top
    plot_bottom = height - margin_bottom

    segments = split_segments(xs, ys)

    svg = []
    svg.append(f'<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}" viewBox="0 0 {width} {height}">')
    svg.append('<rect x="0" y="0" width="100%" height="100%" fill="#ffffff"/>')
    svg.append(f'<text x="{width / 2}" y="32" text-anchor="middle" font-family="Arial" font-size="24" fill="#222">{csv_path.stem}(x)</text>')

    for i in range(11):
        x_tick_value = x_min + (x_max - x_min) * i / 10.0
        x_pixel = map_value(x_tick_value, x_min, x_max, plot_left, plot_right)
        svg.append(f'<line x1="{x_pixel:.2f}" y1="{plot_top}" x2="{x_pixel:.2f}" y2="{plot_bottom}" stroke="#e5e5e5" stroke-width="1"/>')
        svg.append(f'<text x="{x_pixel:.2f}" y="{plot_bottom + 24}" text-anchor="middle" font-family="Arial" font-size="12" fill="#333">{x_tick_value:.2f}</text>')

    for i in range(11):
        y_tick_value = y_min + (y_max - y_min) * i / 10.0
        y_pixel = map_value(y_tick_value, y_min, y_max, plot_bottom, plot_top)
        svg.append(f'<line x1="{plot_left}" y1="{y_pixel:.2f}" x2="{plot_right}" y2="{y_pixel:.2f}" stroke="#e5e5e5" stroke-width="1"/>')
        svg.append(f'<text x="{plot_left - 10}" y="{y_pixel + 4:.2f}" text-anchor="end" font-family="Arial" font-size="12" fill="#333">{y_tick_value:.2f}</text>')

    svg.append(f'<line x1="{plot_left}" y1="{plot_bottom}" x2="{plot_right}" y2="{plot_bottom}" stroke="#444" stroke-width="1.5"/>')
    svg.append(f'<line x1="{plot_left}" y1="{plot_top}" x2="{plot_left}" y2="{plot_bottom}" stroke="#444" stroke-width="1.5"/>')

    for segment in segments:
        points = []
        for x, y in segment:
            px = map_value(x, x_min, x_max, plot_left, plot_right)
            py = map_value(y, y_min, y_max, plot_bottom, plot_top)
            points.append(f"{px:.2f},{py:.2f}")
        svg.append(f'<polyline points="{" ".join(points)}" fill="none" stroke="#1155cc" stroke-width="2"/>')

    svg.append(f'<text x="{(plot_left + plot_right) / 2}" y="{height - 20}" text-anchor="middle" font-family="Arial" font-size="14" fill="#222">x</text>')
    svg.append(f'<text x="24" y="{(plot_top + plot_bottom) / 2}" text-anchor="middle" transform="rotate(-90 24 {(plot_top + plot_bottom) / 2})" font-family="Arial" font-size="14" fill="#222">y</text>')
    svg.append('</svg>')

    out_file = output_dir / f"{csv_path.stem}.svg"
    out_file.write_text("\n".join(svg), encoding="utf-8")


def main():
    if len(sys.argv) < 3:
        print("Usage: python scripts/plot_csv.py <csv_dir> <plots_dir>")
        raise SystemExit(1)

    csv_dir = Path(sys.argv[1])
    plots_dir = Path(sys.argv[2])
    plots_dir.mkdir(parents=True, exist_ok=True)

    for csv_path in sorted(csv_dir.glob("*.csv")):
        plot_file(csv_path, plots_dir)
        print(f"Plotted: {csv_path.name}")


if __name__ == "__main__":
    main()