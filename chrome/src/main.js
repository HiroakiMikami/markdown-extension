
 
document.OnLoad = start();

function start() {
    var markdown = ""
    var xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.onreadystatechange = function() {
	var READYSTATE_COMPLETED = 4;
	var HTTP_STATUS_OK = 200;
	if( this.readyState == READYSTATE_COMPLETED  && this.status == HTTP_STATUS_OK ) {
            // レスポンスの表示
	    document.documentElement.innerHTML = this.responseText
	} else if ( this.readyState == READYSTATE_COMPLETED && this.status == 0 ) {
	    // markdownを得る.
	    markdown = this.responseText;
	    
	    // httpServerにPOSTを飛ばす.
	    xmlHttpRequest.open( 'POST', 'http://localhost:8080/post' );
	    xmlHttpRequest.send(markdown);
	} else {
	    console.log("maybe error")
	}
    }
 
    document.charset = "utf-8"

    // http越しにmarkdownを取得(文字コード変換とかがやりやすい.
    xmlHttpRequest.open('GET', location.href)
    xmlHttpRequest.send("")
}
