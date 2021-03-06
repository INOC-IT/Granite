package ru.sgu.csit.inoc.deansoffice.servlets;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.sgu.csit.inoc.deansoffice.dao.ReferenceDAO;
import ru.sgu.csit.inoc.deansoffice.dao.StudentDAO;
import ru.sgu.csit.inoc.deansoffice.domain.Reference;
import ru.sgu.csit.inoc.deansoffice.domain.Student;
import ru.sgu.csit.inoc.deansoffice.domain.StudentDossier;
import ru.sgu.csit.inoc.deansoffice.services.PhotoService;
import ru.sgu.csit.inoc.deansoffice.services.ReferenceService;
import ru.sgu.csit.inoc.deansoffice.services.StudentDossierService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: hd (KhurtinDN(a)gmail.com)
 * Date: Oct 5, 2010
 * Time: 9:11:09 AM
 */
public class DocumentServlet extends HttpServlet {
    private static final String DOC = "/documents/";

    private ReferenceService referenceService;
    private StudentDossierService studentDossierService;

    private ReferenceDAO referenceDAO;
    private StudentDAO studentDAO;
    private PhotoService photoService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext applicationContext = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());

        referenceDAO = applicationContext.getBean(ReferenceDAO.class);
        studentDAO = applicationContext.getBean(StudentDAO.class);
        photoService = applicationContext.getBean(PhotoService.class);

        referenceService = applicationContext.getBean(ReferenceService.class);
        studentDossierService = applicationContext.getBean(StudentDossierService.class);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String requestURI = request.getRequestURI();

        int index = requestURI.indexOf(DOC);

        if (index < 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String documentType = requestURI.substring(index + DOC.length());

        if (documentType.startsWith("references.pdf")) {

            String idsLine = request.getParameter("referencesId");
            if (idsLine == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            List<Long> referenceIdList = new ArrayList<Long>();
            try {
                String[] ids = idsLine.split(",");
                for (String id : ids) {
                    id = id.trim();
                    if (!id.isEmpty()) {
                        referenceIdList.add(Long.parseLong(id));

                    }
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

//            String documentName = documentType.substring(0, documentType.lastIndexOf(".pdf"));
//
//            URL url = DocumentServlet.class.getResource("/templates/" + documentName + ".xml");
//            if (url == null) {
//                response.sendError(HttpServletResponse.SC_NOT_FOUND);
//                return;
//            }
//            String templateName = url.getFile().replace("%20", " ");

//            Student student = studentDAO.findById(studentId);
//
//            if (student == null) {
//                response.sendError(HttpServletResponse.SC_NOT_FOUND);
//                return;
//            }
//
//            Reference reference = new Reference();
//            reference.setPrintTemplate(new Template(templateName));
//            reference.setOwnerId(studentId);

            List<Reference> referenceList = new ArrayList<Reference>(referenceIdList.size());
            for (Long referenceId : referenceIdList) {
                Reference reference = referenceDAO.findById(referenceId);
                if (reference != null) {
                    referenceList.add(reference);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }

            response.setContentType("application/pdf");
            OutputStream outputStream = response.getOutputStream();

            referenceService.generatePrintForm(referenceList, outputStream);

            outputStream.flush();
        } else if (documentType.startsWith("dossier")) {

            Long studentId;
            try {
                studentId = Long.parseLong(request.getParameter("studentId"));
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String documentName = documentType.substring(0, documentType.lastIndexOf(".pdf"));

            URL url = DocumentServlet.class.getResource("/templates/" + documentName + ".xml");
            if (url == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
//            String templateName = url.getFile().replace("%20", " ");

            Student student = studentDAO.findById(studentId);

            if (student == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (student.getAdditionalData() != null && student.getAdditionalData().getPhoto() != null) {
                photoService.loadData(student.getAdditionalData().getPhoto());
            }

            response.setContentType("application/pdf");
            OutputStream outputStream = response.getOutputStream();

            studentDossierService.generatePrintForm(new StudentDossier(), student, outputStream);

            outputStream.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
