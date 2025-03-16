package com.mart.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mart.config.Config;
import com.mart.response.ResponsePayment;
import com.mart.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	OrderService orderService;

	@GetMapping("/create")
	public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestParam long amount,
			@RequestParam int vnp_TxnRef) throws UnsupportedEncodingException {
//		long amount = Integer.parseInt(req.getParameter("amount")) * 100;
//		long amount = 20000000;
//		String bankCode = req.getParameter("bankCode");

//		String vnp_TxnRef = Config.getRandomNumber(8);
		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", Config.vnp_Version);
		vnp_Params.put("vnp_Command", Config.vnp_Command);
		vnp_Params.put("vnp_TmnCode", Config.vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount));
		vnp_Params.put("vnp_CurrCode", "VND");
		vnp_Params.put("vnp_BankCode", Config.vnp_BankCode);
		vnp_Params.put("vnp_TxnRef", String.valueOf(vnp_TxnRef));
		vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
		vnp_Params.put("vnp_OrderType", Config.orderType);
		vnp_Params.put("vnp_Locale", "vn");
		vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
		String clientIp = Config.getIpAddress(request);
		vnp_Params.put("vnp_IpAddr", clientIp);

		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		cld.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				// Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
		String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

		ResponsePayment responsePayment = new ResponsePayment();
		responsePayment.setStatus("OK");
		responsePayment.setMessage("Successfully");
		responsePayment.setURL(paymentUrl);
		return ResponseEntity.status(HttpStatus.OK).body(responsePayment);
	}

	@PostMapping("/ipn")
	public ResponseEntity<String> handleVnpIpn(@RequestBody Map<String, String> params) {

		String vnp_TxnRef = params.get("vnp_TxnRef"); // Mã đơn hàng
		String vnp_ResponseCode = params.get("vnp_ResponseCode"); // Mã phản hồi từ VNPAY
		String vnp_TransactionStatus = params.get("vnp_TransactionStatus"); // Trạng thái giao dịch
		String vnp_SecureHash = params.get("vnp_SecureHash"); // Chữ ký VNPAY gửi
		String vnp_Amount = params.get("vnp_Amount"); // Số tiền giao dịch

		// Kiểm tra chữ ký (Security Check)
		if (!validateSignature(params, vnp_SecureHash)) {
			return ResponseEntity.badRequest().body("Invalid signature!");
		}

		// Xử lý trạng thái thanh toán
		if ("00".equals(vnp_TransactionStatus)) {
			// Thanh toán thành công
			orderService.updateOrderPaymentStatus(vnp_TxnRef, "Đã Thanh Toán");
			return ResponseEntity.ok("Payment successfully processed");
		} else {
			// Thanh toán thất bại hoặc bị hủy
			orderService.updateOrderPaymentStatus(vnp_TxnRef, "Thanh toán online");
			return ResponseEntity.ok("Payment failed");
		}
	}

	// Hàm kiểm tra chữ ký (Bạn cần triển khai kiểm tra thật sự)
	private boolean validateSignature(Map<String, String> params, String vnp_SecureHash) {
		return true; // Giả sử luôn đúng (Bạn cần triển khai kiểm tra chữ ký)
	}

}
