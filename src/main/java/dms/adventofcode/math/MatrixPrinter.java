package dms.adventofcode.math;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

public class MatrixPrinter {

        public enum Format { JSON, COMPACT_JSON, GRID, CSV, LATEX, LATEX_MD, BOTH }

        public static void print(MatrixInt matrix) {
            print(matrix, Format.BOTH);
        }

        public static void print(MatrixInt matrix, Format format) {
            try {
                // Defaults
                String name = "A";
                String semantics = "integer matrix";
                int padding = 1;

                // Validate matrix rectangularity
                ensureRectangular(matrix.data());

                switch (format) {
                    case JSON:
                        System.out.println(toExactPrettyJson(name, matrix.data(), semantics));
                        break;
                    case COMPACT_JSON:
                        System.out.println(toCompactJson(name, matrix.data(), semantics));
                        break;
                    case GRID:
                        printAligned(name, matrix.data(), padding);
                        break;
                    case CSV:
                        System.out.println(toCsv(matrix.data()));
                        break;
                    case LATEX:
                        System.out.println(toLatexBmatrix(name, matrix.data()));
                        break;
                    case LATEX_MD:
                        System.out.println(toLatexBmatrixMarkdown(name, matrix.data()));
                        break;
                    case BOTH:
                    default:
                        printAligned(name, matrix.data(), padding);
                        System.out.println();
                        System.out.println(toExactPrettyJson(name, matrix.data(), semantics));
                        break;
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        // ---- Formats ------------------------------------------------------------

        // Pretty JSON with Jackson, exact layout: each row inline on its own line; shape inline.
        static String toExactPrettyJson(String name, int[][] m, String semantics) throws IOException {
            ObjectMapper mapper = new ObjectMapper();

            DefaultPrettyPrinter pp = new DefaultPrettyPrinter()
                    .withObjectIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)
                    .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

            StringWriter out = new StringWriter();
            JsonGenerator g = mapper.getFactory().createGenerator(out);
            g.setPrettyPrinter(pp);

            g.writeStartObject();

            // "A": [ ... ]
            g.writeFieldName(name);
            g.writeStartArray();
            for (int[] row : m) {
                // Render row as single-line inline array
                g.writeRawValue(inlineRow(row));
            }
            g.writeEndArray();

            // "shape": [rows, cols] inline
            g.writeFieldName("shape");
            g.writeRawValue("[" + m.length + ", " + m[0].length + "]");

            // "semantics": "..."
            g.writeStringField("semantics", semantics);

            g.writeEndObject();
            g.flush();
            return out.toString();
        }

        // Compact JSON (one line), still using Jackson
        static String toCompactJson(String name, int[][] m, String semantics) {
            String sb = "{" +
                    '"' + name + '"' + ':' +
                    matrixToJsonArray(m) + // [[...],[...],...]
                    ",\"shape\":[" + m.length + ',' + m[0].length + ']' +
                    ",\"semantics\":\"" + escape(semantics) + "\"" +
                    '}';
            return sb;
        }

        // CSV
        static String toCsv(int[][] m) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[i].length; j++) {
                    if (j > 0) sb.append(',');
                    sb.append(m[i][j]);
                }
                if (i < m.length - 1) sb.append('\n');
            }
            return sb.toString();
        }

        // LaTeX (for Markdown)
        static String toLatexBmatrix(String name, int[][] m) {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(" = \\begin{bmatrix}\n");
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[i].length; j++) {
                    if (j > 0) sb.append(" & ");
                    sb.append(m[i][j]);
                }
                sb.append(i < m.length - 1 ? " \\\\\n" : "\n");
            }
            sb.append("\\end{bmatrix}");
            return sb.toString();
        }


    // LaTeX for Markdown inline math — wraps with $...$ and uses &amp; between columns.
    static String toLatexBmatrixMarkdown(String name, int[][] m) {
        StringBuilder sb = new StringBuilder();
        sb.append('$').append(name).append(" = \\begin{bmatrix}\n");
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                if (j > 0) sb.append(" &amp; ");
                sb.append(m[i][j]);
            }
            sb.append(i < m.length - 1 ? " \\\\\n" : "\n");
        }
        sb.append("\\end{bmatrix}$");
        return sb.toString();
    }


    // Human-friendly aligned grid with auto-sized columns
        static void printAligned(String name, int[][] m, int padding) {
            int rows = m.length, cols = m[0].length;
            int[] width = new int[cols];

            for (int j = 0; j < cols; j++) {
                int w = 0;
                for (int[] ints : m) {
                    int len = String.valueOf(ints[j]).length();
                    if (len > w) w = len;
                }
                width[j] = w;
            }

            String colPad = " ".repeat(Math.max(0, padding));
            StringBuilder fmt = new StringBuilder();
            for (int j = 0; j < cols; j++) {
                if (j > 0) fmt.append(colPad);
                fmt.append('%').append(width[j]).append('d');
            }

            System.out.println(name + " = [");
            for (int[] ints : m) {
                System.out.print("  ");
                Object[] row = new Object[cols];
                for (int j = 0; j < cols; j++) row[j] = ints[j];
                System.out.printf((fmt) + "%n", row);
            }
            System.out.println("]");
        }

        // ---- Helpers ------------------------------------------------------------

        static Format parseFormat(String s) {
            String k = s.toLowerCase(Locale.ROOT).replace('-', '_');
            return switch (k) {
                case "json" -> Format.JSON;
                case "compact_json", "compactjson" -> Format.COMPACT_JSON;
                case "grid" -> Format.GRID;
                case "csv" -> Format.CSV;
                case "latex" -> Format.LATEX;
                case "both" -> Format.BOTH;
                default -> throw new IllegalArgumentException("Unknown format: " + s);
            };
        }

        static void ensureRectangular(int[][] m) {
            if (m == null || m.length == 0) throw new IllegalArgumentException("matrix is empty");
            int cols = m[0].length;
            for (int i = 1; i < m.length; i++) {
                if (m[i].length != cols) throw new IllegalArgumentException("matrix is ragged");
            }
        }

        static String inlineRow(int[] row) {
            StringBuilder sb = new StringBuilder(row.length * 3 + 2);
            sb.append('[');
            for (int j = 0; j < row.length; j++) {
                if (j > 0) sb.append(", ");
                sb.append(row[j]);
            }
            sb.append(']');
            return sb.toString();
        }

        static String matrixToJsonArray(int[][] m) {
            StringBuilder sb = new StringBuilder(m.length * (m[0].length * 2 + 3) + 2);
            sb.append('[');
            for (int i = 0; i < m.length; i++) {
                if (i > 0) sb.append(',');
                sb.append('[');
                for (int j = 0; j < m[i].length; j++) {
                    if (j > 0) sb.append(',');
                    sb.append(m[i][j]);
                }
                sb.append(']');
            }
            sb.append(']');
            return sb.toString();
        }

        static String escape(String s) {
            if (s == null) return "";
            StringBuilder out = new StringBuilder(s.length() + 8);
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                switch (c) {
                    case '"':  out.append("\\\""); break;
                    case '\\': out.append("\\\\"); break;
                    case '\n': out.append("\\n"); break;
                    case '\r': out.append("\\r"); break;
                    case '\t': out.append("\\t"); break;
                    default:   out.append(c);
                }
            }
            return out.toString();
        }
    }
