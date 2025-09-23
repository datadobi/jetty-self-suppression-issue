package com.datadobi.bugreports.uploaddemo;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload2.core.FileItemInputIterator;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;

import java.io.IOException;
import java.io.PrintWriter;

public class UploadServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Hello World</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // use this with, for example, curl:
        // curl -v -F upload=@large_file.bin http://localhost:8080/upload
        try {
            if (JakartaServletFileUpload.isMultipartContent(request)) {
                JakartaServletFileUpload<?, ?> upload = new JakartaServletFileUpload<>();
                for (FileItemInputIterator it = upload.getItemIterator(request); it.hasNext(); ) {
                    var item = it.next();
                    if (!item.isFormField()) {
                        byte[] buffer = new byte[1024];
                        try (var is = item.getInputStream()) {
                            long total = 0;
                            int read;
                            while ((read = is.read(buffer)) != -1) {
                                total += read;
                            }
                            response.setHeader("X-Content-Length-Echo", Long.toString(total));
                            System.out.println("total read: " + total);
                        }
                    }
                }
                response.sendRedirect("/upload");
            }
        } catch (IOException e) {
            System.err.println("Regular IO Exception: " + e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            // Because of the try-with-resources usage of the input stream,
            // an IllegalArgumentException is thrown regarding self-suppressing
            // exceptions.
            System.err.println("Unexpected error occurred. " + e.getMessage());
            e.printStackTrace();
        }
    }
}