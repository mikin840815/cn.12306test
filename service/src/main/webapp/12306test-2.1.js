/*
 * version v2.1
 * Copyright © 2016 http://www.12306test.com, All rights reserved
 */
(function ($) {
    $.fn.extend({
        "validate": function (options) {
            if (!isValid(options))
                return this;
            var opts = $.extend({
            	css:{
            		padding:5,
            		width:67,
            		height:67
            	}
            }, defaluts, options);
            return this.each(function () {
                var $this = $(this);
                $this.css({
                	position:'relative'
                });
                $this.attr('_padding', opts.css.padding);
                $this.attr('_width', opts.css.width);
                $this.attr('_height', opts.css.height);
                $this.html("<span style='position:absolute;right:6px;top:6px;'><a href=\"javascript:;\" style=\"font-family:微软雅黑;font-size:12px;color:#666666;\"></a></span><img src=\"javascript:;\" />");
                $this.find("a").click(function(){
                	$.fn.validate.verifyCode($this, opts);
                });
                $this.find("img").click(function(e) {
    				var offset = $(this).offset();
    				var x = (e.pageX - offset.left)-($(this).parent().find("b").width()/2);
    				var y = (e.pageY - offset.top)-($(this).parent().find("b").height()/2);
    				$(this).parent().append("<b style='position:absolute;left:"+x+"px;top:"+y+"px;color:red;cursor:default;' onclick='$.fn.validate.del(this)'>"+opts.select+"</b>");
    			});
                $.fn.validate.verifyCode($this, opts);
            });

        }
    });
    function isValid(options) {
        return !options || (options && typeof options === "object") ? true : false;
    }
    function getIndex($this, x ,y){
    	var width = parseInt($this.attr("_width"));
    	var height = parseInt($this.attr("_height"));
    	var padding = parseInt($this.attr("_padding"));
    	var style = $this.find('img').attr('style');
    	var _height = 25;
    	var index = null;
    	if(style=="eight"){
    		if((width*0+padding*1)<=x && x<=(width*1+padding*1) && (_height+padding)<=y && y<=(_height+padding+height)){
    			index = 0;
    		}else if((width*1+padding*3)<=x && x<=(width*2+padding*3) && (_height+padding)<=y && y<=(_height+padding+height)){
    			index = 1;
    		}else if((width*2+padding*5)<=x && x<=(width*3+padding*5) && (_height+padding)<=y && y<=(_height+padding+height)){
    			index = 2;
    		}else if((width*3+padding*7)<=x && x<=(width*4+padding*7) && (_height+padding)<=y && y<=(_height+padding+height)){
    			index = 3;
    		}else if((width*0+padding*1)<=x && x<=(width*1+padding*1) && (_height+height+padding*3)<=y && y<=(_height+height*2+padding*3)){
    			index = 4;
    		}else if((width*1+padding*3)<=x && x<=(width*2+padding*3) && (_height+height+padding*3)<=y && y<=(_height+height*2+padding*3)){
    			index = 5;
    		}else if((width*2+padding*5)<=x && x<=(width*3+padding*5) && (_height+height+padding*3)<=y && y<=(_height+height*2+padding*3)){
    			index = 6;
    		}else if((width*3+padding*7)<=x && x<=(width*4+padding*7) && (_height+height+padding*3)<=y && y<=(_height+height*2+padding*3)){
    			index = 7;
    		}
    	}else if(style=="six"){
    		if((width*0+padding*1)<=x && x<=(width*1+padding*1) && (_height+padding)<=y && y<=(_height+padding+height)){
    			index = 0;
    		}else if((width*1+padding*3)<=x && x<=(width*2+padding*3) && (_height+padding)<=y && y<=(_height+padding+height)){
    			index = 1;
    		}else if((width*2+padding*5)<=x && x<=(width*3+padding*5) && (_height+padding)<=y && y<=(_height+padding+height)){
    			index = 2;
    		}else if((width*0+padding*1)<=x && x<=(width*1+padding*1) && (_height+height+padding*3)<=y && y<=(_height+height*2+padding*3)){
    			index = 3;
    		}else if((width*1+padding*3)<=x && x<=(width*2+padding*3) && (_height+height+padding*3)<=y && y<=(_height+height*2+padding*3)){
    			index = 4;
    		}else if((width*2+padding*5)<=x && x<=(width*3+padding*5) && (_height+height+padding*3)<=y && y<=(_height+height*2+padding*3)){
    			index = 5;
    		}
    	}else if(style=="nine"){
    		if((width*0+padding*1)<=x && x<=(width*1+padding*1) && (_height+padding)<=y && y<=(_height+padding+height)){
    			index = 0;
    		}else if((width*1+padding*3)<=x && x<=(width*2+padding*3) && (_height+padding)<=y && y<=(_height+padding+height)){
    			index = 1;
    		}else if((width*2+padding*5)<=x && x<=(width*3+padding*5) && (_height+padding)<=y && y<=(_height+padding+height)){
    			index = 2;
    		}else if((width*0+padding*1)<=x && x<=(width*1+padding*1) && (_height+height+padding*3)<=y && y<=(_height+height*2+padding*3)){
    			index = 3;
    		}else if((width*1+padding*3)<=x && x<=(width*2+padding*3) && (_height+height+padding*3)<=y && y<=(_height+height*2+padding*3)){
    			index = 4;
    		}else if((width*2+padding*5)<=x && x<=(width*3+padding*5) && (_height+height+padding*3)<=y && y<=(_height+height*2+padding*3)){
    			index = 5;
    		}else if((width*0+padding*1)<=x && x<=(width*1+padding*1) && (_height+height*2+padding*3)<=y && y<=(_height+height*3+padding*3)){
    			index = 6;
    		}else if((width*1+padding*3)<=x && x<=(width*2+padding*3) && (_height+height*2+padding*3)<=y && y<=(_height+height*3+padding*3)){
    			index = 7;
    		}else if((width*2+padding*5)<=x && x<=(width*3+padding*5) && (_height+height*2+padding*3)<=y && y<=(_height+height*3+padding*3)){
    			index = 8;
    		}
    	}
		return index;
    }
    $.fn.validate.del = function(o) {
        $(o).remove();
    }
    $.fn.validate.verifyCode = function ($this, opts) {
    	$this.find('img').removeAttr('offset');
    	$this.find('b').remove();
		var vcimgUrl = defaluts.api+"?publicKey="+defaluts.publicKey+"&padding="+$this.attr('_padding')+"&width="+$this.attr('_width')+"&height="+$this.attr('_height')+"&r=" + Math.random();
		$.ajax({
			type:"get",
			url:vcimgUrl,
			dataType:"jsonp",
			jsonp: "jsonpCallback",
			success:function(result){
				var img = $this.find('img');
				img.attr("src", "data:image/jpeg;base64,"+result.data+"");
				img.attr("uuid", result.uuid);
				img.attr("style", result.style);
				$this.css({"width":result.width+"px"});
				$this.find("span a").html("刷新");
				$this.append("<b style='display:none;'>"+opts.select+"</b>");
			}
		});
	}
    $.fn.validate.clear = function (id){
    	$("#"+id).find("div").remove();
    }
    $.fn.validate.reflush = function (id){
    	$("#"+id).find("span > a").click();
    }
    $.fn.validate.submit = function ($this, action, data, callback){
		if($this.find("b:visible").length==0){
			$this.append("<div style=\"width: "+$this.find("img").width()+"px; height: "+$this.find("img").height()+"px; position: absolute; top: 0px; left: 0px; background-color: #666666; z-index: 5000; opacity: 0.7; display: none; background-position: initial initial; background-repeat: initial initial;color:#ffffff;line-height:"+$this.find("img").height()+"px;font-size:16px;text-align:center;\">请点击相应答案!</div>");
			$this.find("div").show();
			setTimeout("$.fn.validate.clear('"+$this.attr('id')+"')", 1000);
			return;
		}
		var arr = new Array("","","","","","","","");
		$this.find("b:visible").each(function(){
			var position = $(this).position();
			var index = getIndex($this, position.left, position.top);
			arr.splice(index, 1, (position.left+","+position.top));
		});
		var offset = new Array();
		for (var i=0;i<arr.length;i++) {
		    if (arr[i]!="" && typeof arr[i] != "undefined") {
		    	offset.push(arr[i]);
		    }
		}
		$.ajax({
			type:"POST",
			data:data,
			url:action+"?offset="+offset.join(",")+"&uuid="+$this.find('img').attr('uuid')+"&publicKey="+defaluts.publicKey+"&width="+$this.attr('_width')+"&height="+$this.attr('_height')+"&padding="+$this.attr('_padding')+"&r=" + Math.random(),
			dataType:"text",
			success:function(status){
				callback(status);
			}
		});
	}
})(window.jQuery);