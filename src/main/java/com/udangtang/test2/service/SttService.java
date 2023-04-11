package com.udangtang.test2.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class SttService {
    // 인증 토근값 추출(최초 1회용)
    public String test() throws IOException, JSONException{
        URL url = new URL("https://openapi.vito.ai/v1/authenticate");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("accept", "application/json");
        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpConn.setDoOutput(true);

        String data = "client_id=xYcnj6faG7iupuUs4bIo&client_secret=OoAO8RPfkkidvPY0MRuUNezSSuvEiB-tGPu7qu4o";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = httpConn.getOutputStream();
        stream.write(out);

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        s.close();


        JSONObject jsonObject = new JSONObject(response);
        System.out.println(jsonObject);
        response = (String) jsonObject.get("access_token");

        System.out.println("-----------------------");
        System.out.println(response);
        System.out.println("-----------------------");
        return response;
    }



    // 녹음 파일 분석 후, id값 추출(최초 1회용)
    public String run(String token, File sFile) throws IOException, JSONException {

        URL url = new URL("https://openapi.vito.ai/v1/transcribe");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("accept", "application/json");
        httpConn.setRequestProperty("Authorization", "Bearer "+ token);
        httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=authsample");
        httpConn.setDoOutput(true);

        // 음성 파일 넣어주기
//        File file = new File("./sample6.mp3");
        File file = sFile;

        DataOutputStream outputStream;
        outputStream = new DataOutputStream(httpConn.getOutputStream());

        outputStream.writeBytes("--authsample\r\n");
        outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + file.getName() +"\"\r\n");
        System.out.println(file.getName());
        outputStream.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + "\r\n");
        outputStream.writeBytes("Content-Transfer-Encoding: binary" + "\r\n");
        outputStream.writeBytes("\r\n");
        System.out.println("========< STT 정제 파일...>========");
        System.out.println(file);

        FileInputStream in =new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];

        System.out.println("========< STT 정제 파일의 buffer...>========");
        System.out.println(buffer);

        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1) {
            System.out.println("--------------bytesRead");
            System.out.println(bytesRead);
            System.out.println("--------------bytesRead");
            outputStream.write(buffer,0,bytesRead);
            outputStream.writeBytes("\r\n");
            outputStream.writeBytes("--authsample\r\n");
        }
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes("--authsample\r\n");
        outputStream.writeBytes("Content-Disposition: form-data; name=\"config\"\r\n");
        outputStream.writeBytes("Content-Type: application/json\r\n");
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes("{\n  \"diarization\": {\n");
        outputStream.writeBytes("	\"use_verification\": false\n");
        outputStream.writeBytes("	},\n");
        outputStream.writeBytes("\"use_multi_channel\": false,\n");
        outputStream.writeBytes("\"use_itn\": true,\n");
        outputStream.writeBytes("\"use_disfluency_filter\": true,\n");
        outputStream.writeBytes("\"use_profanity_filter\": false,\n");
        outputStream.writeBytes("\"paragraph_splitter\": {\n");
        outputStream.writeBytes("	\"min\": 10,\n");
        outputStream.writeBytes("	\"max\": 80\n");
        outputStream.writeBytes("	}\n");
        outputStream.writeBytes("}");
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes("--authsample\r\n");
        outputStream.flush();
        outputStream.close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        s.close();

        JSONObject jsonObject = new JSONObject(response);
        System.out.println(jsonObject);
        response = (String) jsonObject.get("id");

        return response;
    }



    // 녹음한 내용 가져오기
    public ArrayList<String> get(String token, String resultID) throws IOException {
        // 문단 별로 담을 list
        ArrayList<String> resultList = new ArrayList<>();
        // STT 결과를 담을 변수 response
        String response = "";

        while(true){
            URL url = new URL("https://openapi.vito.ai/v1/transcribe/"+resultID);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("accept", "application/json");
            httpConn.setRequestProperty("Authorization", "Bearer " + token);

            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            response = s.hasNext() ? s.next() : "";
            s.close();

            System.out.println("========< STT Result...>========");
            System.out.println(response);

            // 만약 변환 중이 아닐 경우, 무한반복 break;
            if (!response.contains("transcribing")) {
                break;
            }
        }

        // 필요한 value 값으로 정제 중..
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = new JSONArray();
            System.out.println(jsonObject);
            jsonObject = jsonObject.getJSONObject("results");   // 결과 ex) [{"msg":"매일 매순간 우리는"}]
            jsonArray = jsonObject.getJSONArray("utterances");  // 결과 ex) {"msg":"매일 매순간 우리는"}

            for(int i=0; i<jsonArray.length(); i++){
                resultList.add(jsonArray.getJSONObject(i).getString("msg"));    // 결과 ex) "매일 매순간 우리는"
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return resultList;
    }
}
