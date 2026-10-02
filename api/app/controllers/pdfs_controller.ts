import PDFDocument from 'pdfkit';
// import type { HttpContext } from '@adonisjs/core/http'

export default class PdfsController {
  public async generateReceipt({ response }) {
    // Cria um novo documento PDF
    const doc = new PDFDocument();

    // Define o cabeçalho para PDF
    response.header('Content-Type', 'application/pdf');
    response.header('Content-Disposition', 'attachment; filename="receipt.pdf"');

    // Armazena o PDF em um buffer antes de enviá-lo na resposta
    const chunks: any[] = [];

    doc.on('data', (chunk) => {
      chunks.push(chunk); // Adiciona pedaços do PDF ao array de chunks
    });

    doc.on('end', () => {
      const pdfBuffer = Buffer.concat(chunks); // Concatena todos os pedaços em um único buffer
      response.send(pdfBuffer); // Envia o PDF na resposta
    });

    // Adicione o conteúdo do PDF
    doc.fontSize(25).text('PDF gerado com AdonisJS e PDFKit!', 100, 100);

    // Finaliza o documento após adicionar o conteúdo
    doc.end();
  }
}
