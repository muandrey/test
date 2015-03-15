function changeState(st)
	{
		if(st == false)
		{
			document.getElementById('state').className = "btn btn-primary btn-danger";
			document.getElementById('state').innerHTML = "Server is not available";
		}
		else
		{
			document.getElementById('state').className = "btn btn-primary btn-success";
			document.getElementById('state').innerHTML = "Server is available";
		}
	};
	function createMessage(msg,id,nickname,date)
	{
		kDiv = document.createElement('div');
		kDiv.id = id;
	    var aDiv = document.createElement('div');
	    aDiv.className = "chat-box-left";
	    aDiv.innerHTML = msg;
	    addButtons(aDiv,id);
	    kDiv.appendChild(aDiv);
	    
	    var bDiv = document.createElement('div');
	    bDiv.className = "chat-box-name-left";
	    var image = document.createElement('img');
	    image.src = "http://vladsokolovsky.com/forums/uploads/profile/photo-36.jpg?_r=0";
	    image.alt = "bootstrap Chat box user image";
	    image.className = "img-circle";
	    bDiv.appendChild(image);
		bDiv.innerHTML += date;
	    bDiv.innerHTML += "- ";
	    bDiv.innerHTML += nickname;
	    kDiv.appendChild(bDiv);
		var cDiv = document.createElement('hr');
	    cDiv.className = "hr-clas";                        
        kDiv.appendChild(cDiv);
        document.getElementById('message').value = "";

        document.getElementById('myChat').appendChild(kDiv);
        document.getElementById('myChat').scrollTop = document.getElementById('myChat').scrollHeight;
	}
	function starting()
	{ 	
		setInterval(getMessages,3000);
	}
	document.onkeyup=function(e)
	{
		e=e||window.event;
		if(e.keyCode === 13)
		{
			sendClick();
		}
	};
	
	function getMessages()
	{
		$.ajax({
                        method: 'GET',
                        url:"?token=TN"+(((+document.getElementById('kol').value)*8)+11)+"EN",
						success:fillWithMessages,
						error:function(){changeState(false);}
						
        });
	};
	function fillWithMessages(messagesJ)
	{
		changeState(true);
		messagesJ=JSON.parse(messagesJ);
		messages=messagesJ.messages;
		$.each(messages,setMessage);
	};
	function setMessage(key,value){
			if(value.Text!="")
			{
				if(document.getElementById(value.messageID)==null)
				{
					createMessage(value.Text, value.messageID, value.NickName,value.addedDate);
					document.getElementById('kol').value = value.messageID;
				}
				else
				{
					saveEdited(value.messageID,value.Text);
				}
			}
			else
				deleteMessageDO(value.messageID);			
		}
	function sendClick() {
		$.ajax({
                        method: 'POST',
                        data: JSON.stringify({ Text: document.getElementById('message').value,
												NickName: document.getElementById('nick').value,
												addedDate: new Date().toLocaleString()})
        });
		document.getElementById('message').value="";
		setTimeout(function(){ getMessages();},100);
    };
    function deleteMessage(id)
    {
    	$.ajax({method:'DELETE',
				url:"?id="+id
		});
		setTimeout(function(){ getMessages();},100);
    };
	function deleteMessageDO(id)
	{
		var b = document.getElementById('myChat');
		element = document.getElementById(id)
		element && element.parentNode.removeChild(element);
	}
	
    function editMessage(id)
    {
    	var elem = document.getElementById(id.slice(1));
    	(element = document.getElementById(id)).parentNode.removeChild(element);
    	var elema = elem.getElementsByClassName("chat-box-left")[0].innerHTML;
    	var message = elema.slice(0,elema.search('<'));
    	elem.getElementsByClassName("chat-box-left")[0].innerHTML = "";
    	var input = document.createElement('input');
    	input.type = "text";
    	input.className = "form-control";
    	input.value = message.trim();
    	input.id = id;
    	var inpGr = document.createElement('input-group');
    	inpGr.appendChild(input);
    	var but = document.createElement('span');
    	but.className = "input-group-btn";
    	var butSave = document.createElement('button');
    	butSave.className = "btn btn-info";
    	butSave.value = "Save";
    	butSave.id = id;
    	butSave.innerHTML = "Save";
    	butSave.addEventListener("click", function(){ save(id); });
    	but.appendChild(butSave);
    	inpGr.appendChild(but);
    	elem.getElementsByClassName("chat-box-left")[0].appendChild(inpGr);
    };
	function saveEdited(id,text)
    {
        var elem = document.getElementById(id);
		var elemt = elem.getElementsByClassName("chat-box-left")[0];
		elem.getElementsByClassName("chat-box-left")[0].innerHTML = text;
		addButtons(elemt, id);
    }
    function save(id)
    {
		id=id.slice(1);
		var elem = document.getElementById(id);
    	var elemt = elem.getElementsByClassName("chat-box-left")[0];
    	var msg = elemt.getElementsByClassName("form-control")[0].value;
    	elem.getElementsByClassName("chat-box-left")[0].innerHTML = msg;
    	addButtons(elemt, id);
		$.ajax({
                        method: 'PUT',
                        data: JSON.stringify({ Text: msg,
												NickName: document.getElementById('nick').value,
												addedDate: new Date().toLocaleString(),
												messageID: id})
        });
		setTimeout(function(){ getMessages();},100);

    };
    function addButtons(prev, id)
    {
	    var buttonRemove = document.createElement('button');
    	buttonRemove.className = "btn pull-right";
    	buttonRemove.id = id;
    	buttonRemove.style.marginTop= "-10px";
    	buttonRemove.addEventListener("click", function(){ deleteMessage(id); });
    	var but = document.createElement('span');
    	but.className = "glyphicon glyphicon-remove";
    	buttonRemove.appendChild(but);
    	prev.appendChild(buttonRemove);

    	var buttonEdit = document.createElement('button');
    	buttonEdit.className = "btn pull-right";
    	buttonEdit.id = 'd'+id;
    	buttonEdit.style.marginTop= "-10px";
    	buttonEdit.addEventListener("click", function(){ editMessage('d'+id); });
    	var but1 = document.createElement('span');
    	but1.className = "glyphicon glyphicon-pencil";
    	buttonEdit.appendChild(but1);
    	prev.appendChild(buttonEdit);

    };
    function login()
    {
    	document.getElementById('nick').value = document.getElementById('NickName').value;
    	element = document.getElementById('forLog');
    	element.innerHTML = "";
    	var lbl = document.createElement('h4');
    	lbl.innerHTML = document.getElementById('nick').value;
    	element.appendChild(lbl);
    	var buttonLog = document.createElement('button');
    	buttonLog.className = "btn pull-right";
    	buttonLog.innerHTML = "Log Out";
    	buttonLog.id = 'logout';
    	buttonLog.style.marginTop= "10px";
    	buttonLog.addEventListener("click", function(){ logout(); });
    	element.appendChild(buttonLog);
    	document.getElementById('forName').appendChild(lbl);
        var buttonEdit = document.createElement('button');
        buttonEdit.className = "btn";
        buttonEdit.innerHTML = "Edit Nick";
        buttonEdit.id = 'editNick';
        buttonEdit.addEventListener("click", function(){ editNick(); });
        document.getElementById('forName').appendChild(buttonEdit);
    };
    function editNick()
    {
        var elem = document.getElementById('forName');
        var nickname = elem.getElementsByTagName('h4')[0].innerHTML;
        elem.innerHTML = "";
        var input = document.createElement('input');
        input.type = "text";
        input.className = "form-control";
        input.value = nickname.trim();
        input.id = "nickEdit";
        var inpGr = document.createElement('input-group');
        inpGr.appendChild(input);
        var but = document.createElement('span');
        but.className = "input-group-btn";
        var butSave = document.createElement('button');
        butSave.className = "btn btn-info";
        butSave.value = "Save";
        butSave.id = 'saveNick';
        butSave.innerHTML = "Save";
        butSave.addEventListener("click", function(){ saveNick(); });
        but.appendChild(butSave);
        inpGr.appendChild(but);
        elem.appendChild(inpGr);
    };
    function saveNick()
    {
        document.getElementById('nick').value = document.getElementById('nickEdit').value;
        element = document.getElementById('forName');
        element.innerHTML = "";
        var lbl = document.createElement('h4');
        lbl.innerHTML = document.getElementById('nick').value;
        element.appendChild(lbl);
        var buttonEdit = document.createElement('button');
        buttonEdit.className = "btn";
        buttonEdit.innerHTML = "Edit Nick";
        buttonEdit.id = 'editNick';
        buttonEdit.addEventListener("click", function(){ editNick(); });
        document.getElementById('forName').appendChild(buttonEdit);
    };
    function logout()
    {
		document.getElementById('nick').value = "";
		document.getElementById('forName').innerHTML = "";
		document.getElementById('forLog').innerHTML = "";
		elem = document.createElement('div');
		elem.className = "form-inline nav nav-pills pull-right";
		elem.style.marginTop = "10px";
		inp = document.createElement('input');
		inp.className = "input-medium search-query";
		inp.placeholder = "NickName";
		inp.id = "NickName";
		inp.type = "text";
		elem.appendChild(inp);
		elem.innerHTML += " ";
		inp2 = document.createElement('input');
		inp2.className = "input-medium search-query";
		inp2.placeholder = "Password";
		inp2.type = "password";
		elem.appendChild(inp2);
		elem.innerHTML += " ";
		elem.innerHTML += " <label class='checkbox'><input type='checkbox'> Remember me</label> ";
		var buttonLog = document.createElement('button');
    	buttonLog.className = "btn btn-primary";
    	buttonLog.innerHTML = "Login";
    	buttonLog.addEventListener("click", function(){ login(); });
    	elem.appendChild(buttonLog);
    	var buttonReg = document.createElement('button');
    	buttonReg.className = "btn btn-primary";
    	buttonReg.innerHTML = "Register";
    	elem.appendChild(buttonReg);
    	document.getElementById('forLog').appendChild(elem);
    };