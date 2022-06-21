/*
 * @(#)MakePdfServlet.java 1.0 2004/02/27
 *
 * Copyright(c) 2003 Universal eXchange Inc.
 * 6F No.20 NanHai Road, JhongJheng District, Taipei 100, Taiwan, R.O.C.
 * All rights reserved.
 */

@SuppressWarnings(value = { "rawtypes", "unchecked", "static-access", "unused" })
@Controller
@RequestMapping(value = "/MakePdfServlet")
public class MakePdfServlet {

	private byte[] makeLcaPdf(LcaWithBLOBs lcaVO, HttpServletRequest request) throws Exception {
		byte[] result = null;

		try {
			parameters.put("papers", lc_conditions);
			parameters.put("specialIndicate", special_conditions);

			JasperPrint jasperPrint = JasperFillManager.fillReport(report_bis, parameters, new JREmptyDataSource());
			Xsl backReportXsl = xslMapper.selectByPrimaryKey(new XslKey());

			if (backReportXsl != null && backReportXsl.getXslFile() != null) {
				ByteArrayInputStream backReportIs = new ByteArrayInputStream(backReportXsl.getXslFile());
				JasperPrint backReport = JasperFillManager.fillReport(backReportIs, parameters, new JREmptyDataSource());
				jasperPrint.addPage((JRPrintPage) backReport.getPages().get(0));
			}

			result = JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		}

		return result;
	}

	private void outputToFile(byte[] report_array) {
		byte[] cloneByteArr = Arrays.copyOf(report_array, report_array.length);
		FileOutputStream fileOutStream = null;

		try {
			fileOutStream = new FileOutputStream(new File(""));
			fileOutStream.write(cloneByteArr);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (fileOutStream != null) {
				try {
					fileOutStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private byte[] makeLcdetailPdf(LcdetailWithBLOBs lcdetailVO, HttpServletRequest request) throws Exception {

		byte[] result = null;

		try {

            JasperPrint jasperPrint = JasperFillManager.fillReport(report_bis, parameters, new JREmptyDataSource());
            Xsl backReportXsl= xslMapper.selectByPrimaryKey(new XslKey());
            Map parametersAdd = new HashMap();
            ByteArrayInputStream backReportIs = new ByteArrayInputStream(backReportXsl.getXslFile());
            JasperPrint backReport = JasperFillManager.fillReport(backReportIs, parametersAdd, new JREmptyDataSource());
            jasperPrint.addPage((JRPrintPage) backReport.getPages().get(0));
            result = JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		}
		return result;
	}

	private byte[] makeLcrPdf(LcrWithBLOBs lcrVO, HttpServletRequest request) throws Exception {
		byte[] result = null;

		try {

			result = JasperRunManager.runReportToPdf(report_bis, parameters, new JREmptyDataSource());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		}
		return result;
	}

	private byte[] makeLcrnPdf(LcrnWithBLOBs lcrnVO, HttpServletRequest request) throws Exception {
		byte[] result = null;

		try {
			result = JasperRunManager.runReportToPdf(report_bis, parameters, new JREmptyDataSource());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		}
		return result;
	}

	private byte[] makeLcdPdf(LcdWithBLOBs lcdVO, HttpServletRequest request) throws Exception {
		byte[] result = null;

		try {
			result = JasperRunManager.runReportToPdf(report_bis, parameters, new JREmptyDataSource());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		}
		return result;
	}

	private void outputPdfStream(HttpServletRequest request, HttpServletResponse response, String filename, byte[] pdf_bytes, String action)
	        throws Exception {
		try {
			response.reset();

			if (this.PDF_ACTION_PRINT.equals(action)) {
				response.setContentType("application/pdf");
				response.setHeader("Content-disposition", "inline; filename=" + filename);
			} else if (this.PDF_ACTION_DOWNLOAD.equals(action)) {
				response.setContentType("application/pdf");
				response.setHeader("Content-disposition", "attachment; filename=" + filename);
			} else {
				throw new Exception("==>PDF ActionType Incorrect...");
			}
			response.setContentLength(pdf_bytes.length);
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(pdf_bytes, 0, pdf_bytes.length);
			ouputStream.flush();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw ex;
		}
	}

}
