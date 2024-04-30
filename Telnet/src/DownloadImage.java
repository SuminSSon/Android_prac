import java.io.*;
import java.net.Socket;

public class DownloadImage {
    public static void main(String[] args) {
        String host = "kiokahn.synology.me";
        int port = 30000;
        String fileURL = "/uploads/-/system/appearance/logo/1/Gazzi_Labs_CI_type_B_-_big_logo.png";
        String filePath = "downloaded_image.png"; // PNG 파일로 저장

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
             BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(filePath))) {

            // Send an HTTP GET request
            out.println("GET " + fileURL + " HTTP/1.1");
            out.println("Host: " + host);
            out.println("Connection: Close");
            out.println();

            // Skip HTTP headers
            skipHeaders(in);

            // Read the response body and write it to file
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }

            System.out.println("PNG file saved successfully.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void skipHeaders(InputStream in) throws IOException {
        // Read until an empty line is encountered, indicating end of headers
        StringBuilder header = new StringBuilder();
        int nextByte;
        while ((nextByte = in.read()) != -1) {
            header.append((char) nextByte);
            if (header.toString().endsWith("\r\n\r\n")) {
                break;
            }
        }
    }
}
