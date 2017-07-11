package controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import data.Message;

@Controller
public class BackNForthController {
	
	//private static final String serverURI;
	
	private List<Message> messages;
	
	@Inject
	private RestTemplate rest;
	
	@Inject
	private ObjectMapper map;
	
	private HttpHeaders hh;
	
	public BackNForthController() {
		messages = new ArrayList<Message>();
		hh = new HttpHeaders();
		hh.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		hh.setContentType(MediaType.APPLICATION_JSON);
	}
	
	// Get main page
	@RequestMapping(value="/main", method=RequestMethod.GET)
	public String Index(){
		return "main";
	}
	
	// Send messages to .Net server
	@RequestMapping(value="/api/messageSend", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Boolean> sendMessage(@RequestBody Message msg)
	{
		
		HttpEntity<Message> mess = new HttpEntity<Message>(msg, hh);
		ResponseEntity<String> rsp = rest.exchange("http://localhost:49759/Home/MessageReceive", HttpMethod.POST, mess, String.class);
		
		if("true".equals(rsp.getBody())){
			messages.add(msg);
			
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		else
			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	
	// Receive messages from the .Net server
	@RequestMapping(value="/api/messageReceive", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Boolean> recieveMessage(@RequestBody Message msg)
	{
		messages.add(msg);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		
	}
	
	
	// Server Sided Event emitter to send messages out to the browser
	@RequestMapping(value="/api/messageQueue", method=RequestMethod.GET)
	public SseEmitter messageQueue(HttpServletRequest req) throws JsonProcessingException, IOException
	{
		ObjectWriter write = map.writer();
		
		SseEmitter sse = new SseEmitter();
		
		sse.send(write.writeValueAsString(messages));
		
		sse.complete();
		
		messages.clear();
		
		return sse;
	}
	
}
