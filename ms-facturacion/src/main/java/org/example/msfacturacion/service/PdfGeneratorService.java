
package org.example.msfacturacion.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.example.msfacturacion.dato.FacturaDTO;
import org.example.msfacturacion.dato.FacturaDetalleDTO;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;

@Service
public class PdfGeneratorService {

    /** Genera un PDF a partir de un FacturaDTO y lo devuelve como arreglo de bytes */
    public byte[] generarFacturaPdf(FacturaDTO factura) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document doc = new Document(PageSize.A4, 36, 36, 45, 36);
            PdfWriter.getInstance(doc, out);
            doc.open();

            /* ─────────── Encabezado ─────────── */
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            doc.add(new Paragraph("FACTURA ELECTRÓNICA", titleFont));
            doc.add(new Paragraph("Número: " + factura.getNumeroFactura()));
            doc.add(new Paragraph("Fecha de emisión: " + factura.getFechaEmision()));
            doc.add(new Paragraph("Moneda: " + factura.getMoneda()));
            doc.add(Chunk.NEWLINE);

            /* ─────────── Datos del cliente ─────────── */
            doc.add(new Paragraph("Cliente: " + factura.getCliente().getNombre()));
            doc.add(new Paragraph("Documento: " + factura.getCliente().getRucDni()));
            doc.add(Chunk.NEWLINE);

            /* ─────────── Tabla de ítems ─────────── */
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4f, 1.2f, 1.5f, 1.5f, 1.7f});

            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED);
            Font headFont = new Font(bf, 10, Font.BOLD);
            addCellHead(table, "Descripción", headFont);
            addCellHead(table, "Cant.", headFont);
            addCellHead(table, "Precio U.", headFont);
            addCellHead(table, "IGV", headFont);
            addCellHead(table, "Total", headFont);

            for (FacturaDetalleDTO it : factura.getItems()) {
                table.addCell(it.getNombreProducto() + (it.getDescripcion() != null ? " - " + it.getDescripcion() : ""));
                table.addCell(String.valueOf(it.getCantidad()));
                table.addCell(String.format("%.2f", it.getPrecioUnitario()));
                table.addCell(String.format("%.2f", it.getIgv()));
                table.addCell(String.format("%.2f", it.getTotalLinea()));
            }
            doc.add(table);
            doc.add(Chunk.NEWLINE);

            /* ─────────── Totales ─────────── */
            doc.add(new Paragraph("Subtotal: S/ " + String.format("%.2f", factura.getSubTotal())));
            doc.add(new Paragraph("IGV: S/ " + String.format("%.2f", factura.getTotalImpuestos())));
            doc.add(new Paragraph("TOTAL: S/ " + String.format("%.2f", factura.getTotal())));

            doc.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
    }

    private void addCellHead(PdfPTable tbl, String txt, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(txt, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new Color(173, 216, 230));  // Light blue RGB
        tbl.addCell(cell);
    }
}
