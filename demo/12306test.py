# coding=utf-8
from flask import Flask
from flask import request
import md5
import httplib

app = Flask(__name__)

private_key = '013f534e8d393eb6fb2d257b807237d5'

@app.route('/', methods=['GET', 'POST'])
def index():
    return '''
	<script type="text/javascript" src="https://code.jquery.com/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="http://www.12306test.com/12306test-2.1.min.js"></script>
	<div id="wb_validate"></div>
	<button onclick="submit()">submit</button>
	<script>
	$(document).ready(function(){
		$("#wb_validate").validate({select:'★'});
	});
	var defaluts = {
		publicKey:'33f354254bec9b722674000f725110c5',
		api : 'http://www.12306test.com/client.validate',
		select : '★'
	};
	function submit() {
		$.fn.validate.submit($("#wb_validate"), "/post", {}, function(status) {
			if (status == 'SUCCESS') {
				alert("验证成功");
			} else if(status == 'AUTH'){
				alert("您的帐号未被审核");
			} else {
				alert("验证失败");
				setTimeout($.fn.validate.verifyCode($("#wb_validate")), 5000);
			}
		});
	}
	</script>
	'''
@app.route('/post', methods=['POST'])
def post():
	data = "publicKey="+request.args.get('publicKey', '')+""
	data += "&"
	data += "offset="+request.args.get('offset', '')+""
	data += "&"
	data += "padding="+request.args.get('padding', '')+""
	data += "&"
	data += "width="+request.args.get('width', '')+""
	data += "&"
	data += "height="+request.args.get('height', '')+""
	data += "&"
	data += "uuid="+request.args.get('uuid', '')+""
	data += "&"
	m1 = md5.new()
	m1.update(data+private_key)
	sign = m1.hexdigest().upper()
	data += "sign="+sign+""
	conn = httplib.HTTPConnection("www.12306test.com:80")
	conn.request("POST", "/client.validate?"+data, '', {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"})  
	response = conn.getresponse()  
	return response.read()

if __name__ == '__main__':
    app.run()