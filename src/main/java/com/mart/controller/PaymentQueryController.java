package com.mart.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mart.config.Config;
import com.mart.response.ResponsePayment;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/query")
public class PaymentQueryController {

	@PostMapping("/payment-status")
	public ResponseEntity<ResponsePayment> Query(HttpServletRequest req, @RequestParam int vnp_TxnRefRes,
			@RequestParam long vnp_TransDateRes, @RequestParam String vnp_SecureHashRes) throws IOException {

		// ✅ 1. Kiểm tra tính hợp lệ của chữ ký bảo mật (Secure Hash)
		String hash_CheckData = String.join("|", String.valueOf(vnp_TxnRefRes), String.valueOf(vnp_TransDateRes));
		String secureHash_Calculated = Config.hmacSHA512(Config.secretKey, hash_CheckData);

		if (!secureHash_Calculated.equals(vnp_SecureHashRes)) {
			return ResponseEntity.badRequest().body(new ResponsePayment("97", "Invalid Secure Hash", null));
		}

		// ✅ 2. Chuẩn bị request gửi đến VNPAY
		String vnp_RequestId = Config.getRandomNumber(8);
		String vnp_Version = "2.1.0";
		String vnp_Command = "querydr";
		String vnp_TmnCode = Config.vnp_TmnCode;
		String vnp_TxnRef = String.valueOf(vnp_TxnRefRes);
		String vnp_OrderInfo = "Kiểm tra kết quả GD OrderId:" + vnp_TxnRef;
		String vnp_TransDate = String.valueOf(vnp_TransDateRes);

		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());

		String vnp_IpAddr = Config.getIpAddress(req);

		JsonObject vnp_Params = new JsonObject();
		vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
		vnp_Params.addProperty("vnp_Version", vnp_Version);
		vnp_Params.addProperty("vnp_Command", vnp_Command);
		vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
		vnp_Params.addProperty("vnp_TransactionDate", vnp_TransDate);
		vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
		vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

		// Kiểm tra dữ liệu trước khi tạo hash
		System.out.println("Data for hash: " + hash_CheckData);
		System.out.println("Expected Secure Hash: " + vnp_SecureHashRes);
		System.out.println("Calculated Secure Hash: " + secureHash_Calculated);

		String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, vnp_TxnRef,
				vnp_TransDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
		String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hash_Data);
		vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

		// ✅ 3. Gửi request đến VNPAY
		URL url = new URL(Config.vnp_ApiUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);

		// Ghi request body
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(vnp_Params.toString());
		wr.flush();
		wr.close();

		// ✅ 4. Nhận phản hồi từ VNPAY
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder response = new StringBuilder();
		String output;
		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();
		con.disconnect(); // ✅ Đóng kết nối

		// ✅ 5. Xử lý phản hồi từ VNPAY
		JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
		String status = jsonResponse.get("vnp_ResponseCode").getAsString();
		String message = jsonResponse.get("vnp_Message").getAsString();
		String paymentUrl = jsonResponse.has("vnp_PaymentUrl") ? jsonResponse.get("vnp_PaymentUrl").getAsString()
				: null;

		if (!status.equals("00")) {
			return ResponseEntity.badRequest()
					.body(new ResponsePayment(status, "Payment query failed: " + message, null));
		}

		return ResponseEntity.ok(new ResponsePayment(status, message, paymentUrl));
	}

}
