var source = new EventSource("/BacknForth/api/messageQueue");

/*source.onmessage = function(e){
	var data = JSON.parse(e.data);
	for(var i = 0; i < data.length; i++)
		$('#messageDisplay').append(data[i].sender + ": " + data[i].message + "\n");
};
*/

var app = angular.module('BackNForthAngularApp', []);

app.controller('BackNForthController', function($http){
	
	var ctrl = this;
	this.message = "";
	this.eSource = source;
	
	this.SubmitMessage = function(){
		if(ctrl.message != ""){
			var message = {
				"sender" : "Java",
				"message" : ctrl.message
			};
			
			
			$http({
				method: "POST",
				url: "/BacknForth/api/messageSend",
				data: JSON.stringify(message)
			});
			
			ctrl.message = "";
		}
	};
	
	this.SubmitIfEnter = function($event) {
		if($event.keyCode == 13)
			ctrl.SubmitMessage();
	};
	
	this.eSource.onmessage = function(e){
		var data = JSON.parse(e.data);
		var messageBox = document.getElementById("messageDisplay");
		for(var i = 0; i < data.length; i++)
			messageBox.value += data[i].sender + ": " + data[i].message + "\n";
	};
	
});

//$(function(){
//	var submitMessage = function() {
//		var message = {
//				"sender" : "Java",
//				"message": $('#messageData').val()
//		};
//		
//		
//		$.ajax({
//			type: "POST",
//			url: "/BacknForth/api/messageSend",
//			contentType: "application/json",
//			data: JSON.stringify(message)
//		});
//		
//		$('#messageData').val('');
//		
//	};
//
//	$('#messageData').keydown(function(e){
//		if(e.keyCode == 13 && $('#messageData').val() != '')
//			submitMessage();
//	});
//
//	$('#submitter').click(function(e){
//		if($('#messageData').val() != '')
//			submitMessage();
//	});
//});


