<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html ng-app='myApp'>
<head>
<title>Hello User</title>
<script>
	var ctx = "${pageContext.request.contextPath}";
</script>
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<link
	href="//netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">

<link
	href="${pageContext.request.contextPath}/resources/css/display.css"
	rel="stylesheet"></link>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.1/angular.min.js"></script>
<script
	src="//ajax.googleapis.com/ajax/libs/angularjs/1.6.1/angular-animate.js"></script>
<script
	src="//ajax.googleapis.com/ajax/libs/angularjs/1.6.1/angular-sanitize.js"></script>
<script
	src="//angular-ui.github.io/bootstrap/ui-bootstrap-tpls-2.5.0.js"></script>
<script
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="http://localhost:3000/socket.io/socket.io.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</head>

<body ng-controller="login" style="height: {{scope.height">
	<h1 style="padding-left: 30%;">Welcome to Global Chat Room</h1>
	<div class="container" ng-show="!mainPage.status">
		<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<div class="panel panel-login">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-6">
								<a href="#" class="active" id="login-form-link">Login</a>
							</div>
							<div class="col-xs-6">
								<a href="#" id="register-form-link">Register</a>
							</div>
						</div>
						<hr>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-12">
								<form id="login-form" method="post" role="form"
									style="display: block;">
									<div class="form-group">
										<input type="text" ng-model="username" id="username"
											tabindex="1" class="form-control" placeholder="Username"
											value="">
									</div>
									<div class="form-group">
										<input type="password" ng-model="password" id="password"
											tabindex="2" class="form-control" placeholder="Password">
									</div>
									<div ng-show="loginFailedPage.status" class="form-group">
										<i></i> Can't find User
									</div>
									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" name="login-submit" id="login-submit"
													tabindex="4" class="form-control btn btn-login"
													value="Log In" ng-click="loginUser()">
											</div>

										</div>
									</div>
								</form>
								<form id="register-form" role="form" style="display: none;">
									<div class="form-group">
										<input type="text" ng-model="username" id="registerusername"
											tabindex="1" class="form-control" placeholder="Username"
											value="">
									</div>
									<div class="form-group">
										<input type="password" ng-model="password"
											id="registerpassword" tabindex="2" class="form-control"
											placeholder="Password">
									</div>
									<div ng-show="userExistedPage.status" class="form-group">
										<i></i> User existed
									</div>
									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" name="register-submit"
													id="register-submit" tabindex="4"
													class="form-control btn btn-register" value="Register Now"
													ng-click="registerUser()">
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal PopUp Starts -->
	<div ng-show="mainPage.status" class="main_section">
		<div class="container">
			<div class="pull-middle">
				<p>
					<strong class="primary-font">Current login as: {{userId}}</strong>
				</p>

				<a class="btn btn-info btn-lg" ng-click="logout()"> <span
					class="glyphicon glyphicon-log-out"></span> Log out
				</a>
				</p>


			</div>
			<div class="chat_container">
				<div class="col-sm-3 chat_sidebar">
					<div class="row">
						<div id="custom-search-input">
							<form class="input-group col-md-12">
								<input type="text" class=" search-query form-control"
									placeholder="Search Channels to Subscribe"
									ng-model="channelidForSearch"
									uib-typeahead="channel for channel in getChannels($viewValue)"
									typeahead-loading="loadingChannels"
									typeahead-no-results="noResults" class="form-control" required>
								<button class="btn btn-danger" type="submit"
									ng-click="updateUser(channelidForSearch)">
									<span class=" glyphicon glyphicon-plus"></span>
								</button>
								<i ng-show="loadingChannels" class="glyphicon glyphicon-refresh"></i>
								<div ng-show="noResults">
									<i class="glyphicon glyphicon-remove"
										ng-click="resetCreateChannelFlag()"></i>
									<div class="alert alert-warning" role="alert">
										Can't find channel with ID:{{channelidForSearch}}.Do you want
										to create a new channel with this ID <a
											class="pull-right btn btn-success"
											ng-click="createChannel(channelidForSearch)"> Create</a>
									</div>
								</div>
								<div ng-show="subscribeFailed.status">
									<i class="glyphicon glyphicon-remove" ng-click="resetFlag()"></i>
									You already in channel
								</div>
							</form>
						</div>
						<div class="member_list">
							<ul class="list-unstyled">
								<li ng-repeat="channel in channels" class="left clearfix"
									ng-class="{'active_chat': channel.channelid == CurrentChannel.channelid}">
									<span class="chat-img pull-left"> <img
										src="${pageContext.request.contextPath}/resources/images/group.jpg"
										alt="User Avatar" class="img-circle">
								</span>
									<div class="chat-body clearfix"
										ng-click="flipChannel(channel.channelid)">
										<div class="header_sec">
											<strong class="primary-font">{{channel.channelid}}</strong>
										</div>
										<div class="contact_sec"  ng-show='channel.unReadMessageCount>0'>
											<span class="badge pull-right" style="background-color: red;">{{channel.unReadMessageCount}}</span>
										</div>
									</div>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<!--chat_sidebar-->


				<div ng-show='CurrentChannel' class="col-sm-9 message_section">
					<div class="row">
						<div class="new_message_head">
							<div class="pull-left">
								<i class="fa fa-plus-square-o" aria-hidden="true"></i> Current
								Channel: {{CurrentChannel.channelid}}
							</div>
						</div>
						<!--new_message_head-->

						<div class="chat_area" id="char_area_scroll"  my-main-directive>
							<ul class="list-unstyled">
								<div class="pull-middle" style="padding-left: 30%;">
									<i class="fa fa-plus-square-o" aria-hidden="true"></i><a
										style="cursor: pointer;"
										ng-click="getPreviousMessage(CurrentChannel.channelid)">
										........Show More History Records........</a>
								</div>
								<li
									ng-repeat="message in CurrentChannel.messages track by $index"
									class="left clearfix admin_chat"><span
									class="chat-img1 pull-left" updateScroll()> <img
										src="${pageContext.request.contextPath}/resources/images/user.jpg"
										alt="User Avatar" class="img-circle"  my-repeat-directive></span>
									<div class="chat-body1 clearfix">
										<div class="chat_time pull-left ng-binding">
											<span class="chat_time pull-left ng-binding" color="b"
												style="color: black; font-weight: bold;">{{message.userid}}</span>
											<span class="chat_time " color="b"
												style="color: black; padding-left: 10">{{convertToLocalTime(message.timeCreated)}}</span>
										</div>
										<p style="padding-top: 20;">{{message.context}}</p>
									</div></li>
							</ul>
						</div>
						<!--chat_area-->
						<div class="message_write">
							<textarea class="form-control" placeholder="type a message"
								ng-model="messageText"></textarea>
							<div class="clearfix"></div>
							<div class="chat_bottom">
								<a href="#" class="pull-right btn btn-success"
									ng-click="sendmessage(messageText)"> Send</a>
							</div>
						</div>
					</div>
				</div>
				<!--message_section-->
			</div>
		</div>
	</div>
</body>
</html>