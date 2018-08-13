var app = angular
    .module('myApp', ['ngAnimate', 'ngSanitize', 'ui.bootstrap']);
app
    .controller(
        'login',
        function($scope, $http, $window, socket, myService) {
            // don't clear $scope.mainPage as it used for display
            $scope.mainPage = {
                status: false
            };
            $scope.loginFailedPage = {
                status: false
            };
            $scope.userExistedPage = {
                status: false
            };
            $scope.subscribeFailed = {
                status: false
            };
            // clear below value when log out
            $scope.username = null;
            $scope.password = null;
            $scope.userId = null;
            $scope.User = null;
            $scope.UserLastLoginTime = null;
            $scope.UserToUpdate = null;
            $scope.UserNewValue = null;
            $scope.initialCurrentPageSetted = false;
            $scope.channels = [];
            $scope.UserChannelTimeStampInfo = new Map();
            $scope.UnReadMessageForAllChannel = new Map();
            $scope.MessageSentByUser = new Map();
            $scope.CurrentChannel = null;
            $scope.height = $window.screen.availHeight;
            $scope.scrollPo = $window.scrollY;
            $scope.errorMessage = null;

			$scope.updateScroll =function(){
			    var element = document.getElementById("char_area_scroll");
			    element.scrollTop = 600;
			}
            // flip between login page and register page
            $('#login-form-link').click(function(e) {
                $("#login-form").delay(100).fadeIn(100);
                $("#register-form").fadeOut(100);
                $('#register-form-link').removeClass('active');
                $(this).addClass('active');
                e.preventDefault();
                $scope.clearLoginInfo();
            });
            $('#register-form-link').click(function(e) {
                $("#register-form").delay(100).fadeIn(100);
                $("#login-form").fadeOut(100);
                $('#login-form-link').removeClass('active');
                $(this).addClass('active');
                e.preventDefault();
                $scope.clearLoginInfo();
            });
            // clear username and password when flip between register
            // and login page
            $scope.clearLoginInfo = function() {
                $scope.username = null;
                $scope.password = null;
                $scope.loginFailedPage.status = false;
                $scope.userExistedPage.status = false;
            }
            $scope.clearDate = function() {
                    $scope.username = null;
                    $scope.password = null;
                    $scope.userId = null;
                    $scope.User = null;
                    $scope.UserLastLoginTime = null;
                    $scope.UserToUpdate = null;
                    $scope.UserNewValue = null;
                    $scope.initialCurrentPageSetted = false;
                    $scope.channels = [];
                    $scope.UserChannelTimeStampInfo = new Map();
                    $scope.UnReadMessageForAllChannel = new Map();
                    $scope.MessageSentByUser = new Map();
                    $scope.CurrentChannel = null;
                    $scope.clearLoginInfo();
                    $scope.mainPage.status = false;
                    $("#login-form").delay(100).fadeIn(100);
                    $("#register-form").fadeOut(100);
                    $('#register-form-link').removeClass('active');
                    $(this).addClass('active');
                }
                // clear data when log out
            $scope.logout = function() {
                var test = 0;
                //update timeStamp for current user
                $scope.UserChannelTimeStampInfo.get($scope.CurrentChannel.channelid).timeUpdated=new Date().getTime();
                var myDataPromise = myService.getData($scope.User.userid, $scope.channels, $scope.UserChannelTimeStampInfo);
                myDataPromise.then($scope.clearDate());
            }
            $scope.convertToLocalTime = function(time) {
                    return new Date(time).toLocaleString('en-US', {
                        hour12: false
                    });
                }
                // close error message when subscribe to a new channel
            $scope.resetFlag = function() {
                $scope.subscribeFailed.status = false;
            };

            $scope.resetCreateChannelFlag = function() {
                $scope.noResults = null;
            };
            // reorganization message sent by current user
            $scope.reOrganizationMessageSentByUser = function(data) {
                $scope.MessageSentByUser = new Map();
                for (var i = 0; i < data.length; i++) {
                    var channelid = data[i].channelid;
                    if ($scope.MessageSentByUser.get(channelid) == null) {
                        var messageList = [];
                        messageList.put(data[i]);
                        $scope.MessageSentByUser.set(channelid,
                            messageList);
                    } else {
                        $scope.MessageSentByUser.get(channelid).put(
                            data[i]);
                    }
                }
            }
            $scope.setUnreadMessageCount = function(channelid, num, reset) {
                    for (var i = 0; i < $scope.channels.length; i++) {
                        var channel = $scope.channels[i];
                        if (channel.channelid == channelid) {
                            if (reset == true)
                                channel.unReadMessageCount = 0;
                            else
                                channel.unReadMessageCount = channel.unReadMessageCount + 1;
                            break;
                        }
                    }
                }
                // when user flip between channel, we need to set current
                // channel in order to display un-read message
            $scope.flipChannel = function(channelid) {
                $scope
                    .setCurrentChannel(channelid, Date.now(),
                        $scope.UserChannelTimeStampInfo
                        .get(channelid).messageList);
                $scope.setUnreadMessageCount(channelid, 0, true);
            }

            // prepare necessary info in CurrentChannel object
            $scope.setCurrentChannel = function(channelid, timeUpdated,
                    messages) {
                    $scope.CurrentChannel = null;
                    if (messages == null)
                        messages = [];
                    var channelInfo = {
                        channelid: channelid,
                        timeUpdated: Date.now(),
                        messages: messages
                    };
                    $scope.CurrentChannel = channelInfo;
                    $scope.setUnreadMessageCount(channelid, 0, true);
                }
                // when user log in, system need to prepare un-read message
                // for all channel
            $scope.reOrganizationUserChannelTimeStampInfo = function(
                data) {
                for (var i = 0; i < data.length; i++) {
                    var messageListWithTimeStamp = {
                        timeUpdated: data[i].timeUpdated,
                        messageList: []
                    };
                    $scope.UserChannelTimeStampInfo
                        .set(data[i].channelid,
                            messageListWithTimeStamp);
                    $scope.getUnReadMessageForChannel($scope.userId,
                        data[i].channelid);
                }
            }

            $scope.getMessageSentByUser = function(channelid, userid) {
                $scope.errorMessage = null;
                url = ctx + '/api/MessageEntries/getMessageSentByUser';
                var time
                $http
                    .get(url, {
                        params: {
                            userid: userid
                        }
                    })
                    .then(
                        function successCallback(response) {
                            $scope.MessageSentByUser = reOrganizationMessageSentByUser(response.data.body);
                        },
                        function errorCallback(response) {
                            $scope.errorMessage = response.data.responseMessage.message;
                        });
            };

            // to get previous history record, we should get first
            // element in existed list which present
            // oldest time, use that to query message and get most
            // recent 100 records back.Add back to the
            // header of existed list
            $scope.getPreviousMessage = function(channelid) {
                var currentList = $scope.UserChannelTimeStampInfo
                    .get($scope.CurrentChannel.channelid).messageList;
                var queryTimeStamp = null;
                if (currentList.length == 0) {
                    queryTimeStamp = new Date().getTime();
                } else {
                    var firstElem = $scope.UserChannelTimeStampInfo
                        .get(channelid).messageList[0];
                    queryTimeStamp = new Date(firstElem.timeCreated).getTime();
                }
                $scope.errorMessage = null;
                url = ctx + '/api/MessageEntries/getPreviousMessage';
                $http
                    .get(url, {
                        params: {
                            userid: $scope.userId,
                            channelid: channelid,
                            timeStamp: queryTimeStamp
                        }
                    })
                    .then(
                        function successCallback(response) {
                            var messageListWithTimeStamp = {
                                timeUpdated: new Date()
                                    .getTime(),
                                messageList: response.data.body
                            };
                            var combinedMessagesList = messageListWithTimeStamp.messageList
                                .concat($scope.UserChannelTimeStampInfo
                                    .get(channelid).messageList);
                            messageListWithTimeStamp.messageList = combinedMessagesList;
                            $scope.UserChannelTimeStampInfo
                                .set(channelid,
                                    messageListWithTimeStamp);
                            $scope.CurrentChannel.messages = $scope.UserChannelTimeStampInfo
                                .get(channelid).messageList;
                        },
                        function errorCallback(response) {
                            $scope.errorMessage = response.data.responseMessage.message;
                        });
            }

            $scope.getUnReadMessageForChannel = function(userid,
                channelid) {
                $scope.errorMessage = null;
                url = ctx +
                    '/api/MessageEntries/getUnReadMessageForChannel';
                var timeUpdated = $scope.UserChannelTimeStampInfo
                    .get(channelid).timeUpdated == null ? new Date()
                    .getTime() :
                    $scope.UserChannelTimeStampInfo
                    .get(channelid).timeUpdated;
                $http
                    .get(url, {
                        params: {
                            userid: userid,
                            channelid: channelid,
                            timeStamp: timeUpdated
                        }
                    })
                    .then(
                        function successCallback(response) {
                            var messageListWithTimeStamp = {
                                timeUpdated: timeUpdated,
                                messageList: response.data.body
                            };
                            $scope.UserChannelTimeStampInfo
                                .set(channelid,
                                    messageListWithTimeStamp);
                            var channelInfo = {
                                channelid: channelid,
                                unReadMessageCount: response.data.body.length
                            }
                            $scope.channels.push(channelInfo);
                            if ($scope.initialCurrentPageSetted == false) {
                                $scope.setCurrentChannel(channelid,
                                    timeUpdated,
                                    response.data.body);
                                $scope.initialCurrentPageSetted = true;
                            }
                        },
                        function errorCallback(response) {
                            $scope.errorMessage = response.data.responseMessage.message;
                        });
            };
            $scope.updateUserChannelTimeStampInfo = function() {
                $scope.errorMessage = null;
                url = ctx + '/api/userChannelTimeStampEntries/updateUserChannelTimeStamp';
                for (var i = 0; i < $scope.channels; i++) {
                    var channelid = $scope.channels[i].id;
                    var userChannelTimeStamp = {
                        userid: $scoep.User.userid,
                        channelid: channelid,
                        timeUpdated: new Date($scope.UserChannelTimeStampInfo.get(channelid).messageListWithTimeStamp.timeUpdated).getTime()
                    }
                    $http
                        .post(url, angular.toJson(userChannelTimeStamp))
                        .then(
                            function successCallback(response) {

                            },
                            function errorCallback(response) {
                                $scope.errorMessage = response.data.responseMessage.message;
                            });
                }
            }
            $scope.sendmessage = function(messageContext) {
                $scope.errorMessage = null;
                url = ctx + '/api/MessageEntries/saveMessage';
                var message = {
                    userid: $scope.User.userid,
                    messageId: Date.now() + ":" + $scope.User.userid,
                    channelid: $scope.CurrentChannel.channelid,
                    context: messageContext,
                    timeCreated: Date.now()
                };
                $http
                    .post(url, angular.toJson(message))
                    .then(
                        function successCallback(response) {
                            $scope.messageText = null;
                            // update messagelist for
                            // currentChannel and whole
                            // ChannelMessageInfo
                            message.timeCreated = new Date()
                                .toLocaleString('en-US', {
                                    hour12: false
                                });
                            $scope.UserChannelTimeStampInfo
                                .get($scope.CurrentChannel.channelid).messageList
                                .push(message);
                            $scope.broadcastMessage(message);
                        },
                        function errorCallback(response) {
                            $scope.User.channels.pop();
                            $scope.errorMessage = response.data.responseMessage.message;
                        });
            };

            $scope.createChannel = function(channelidForSearch) {
                $scope.errorMessage = null;
                url = ctx + '/api/ChannelEntries/createChannel';
                return $http
                    .get(url, {
                        params: {
                            channelid: channelidForSearch
                        }
                    })
                    .then(
                        function successCallback(response) {
                            $scope
                                .updateUser(channelidForSearch);
                            $scope.noResults = null;
                        },
                        function errorCallback(response) {
                            $scope.errorMessage = response.data.responseMessage.message;
                        });
            }

            $scope.updateUser = function(channelid) {
                for (var i = 0; i < $scope.User.channels.length; i++) {
                    if ($scope.User.channels[i].channelid == channelid) {
                        $scope.subscribeFailed.status = true;
                        return;
                    }
                }
                url = ctx + '/api/userEntries/updateUser';
                var newChannelId = {
                    channelid: channelid
                };
                $scope.User.channels.push(newChannelId);
                var formData = new FormData();
                var data = {
                    user: $scope.User,
                    channelid: channelid
                }
                $http
                    .post(url, angular.toJson(data))
                    .then(
                        function successCallback(response) {
                            // once user subscribe a new
                            // channel, we need to update
                            // channelMessageInfo
                            var messageListWithTimeStamp = {
                                timeUpdated: Date.now(),
                                messageList: []
                            };
                            $scope.UserChannelTimeStampInfo
                                .set(channelid,
                                    messageListWithTimeStamp);
                            var channelInfo = {
                                channelid: channelid,
                                unReadMessageCount: 0
                            }
                            $scope.channels.push(channelInfo);
                            $scope.flipChannel(channelid);
                        },
                        function errorCallback(response) {
                            $scope.User.channels.pop();
                            $scope.errorMessage = response.data.responseMessage.message;
                        });
            };

            $scope.getChannels = function(val) {
                $scope.errorMessage = null;
                url = ctx + '/api/ChannelEntries/getChannelWithId';
                return $http.get(url, {
                    params: {
                        channelid: val
                    }
                }).then(function(response) {
                    return response.data.body.map(function(item) {
                        return item.channelid;
                    });
                });
            };
            $scope.getUserChannelTimeStampInfo = function(userid) {
                $scope.errorMessage = null;
                url = ctx +
                    '/api/userChannelTimeStampEntries/getAllForUser';
                $http
                    .get(url, {
                        params: {
                            userId: $scope.userId
                        }
                    })
                    .then(
                        function successCallback(response) {
                            $scope
                                .reOrganizationUserChannelTimeStampInfo(response.data.body);
                        },
                        function errorCallback(response) {
                            $scope.errorMessage = response.data.responseMessage.message;
                        });
            };

            $scope.registerUser = function() {
                $scope.getUser(ctx + '/api/userEntries/registerUser');
            }

            $scope.loginUser = function() {
                $scope.getUser(ctx + '/api/userEntries/getUser');
            }

            $scope.getUser = function(url) {
                $scope.User = null;
                $scope.errorMessage = null;
                $http
                    .get(url, {
                        params: {
                            userId: $scope.username,
                            password: $scope.password
                        }
                    })
                    .then(
                        function successCallback(response) {
                            $scope.User = response.data.body;
                            $scope.userId = $scope.User.userid;
                            $scope.UserLastLoginTime = new Date(
                                    $scope.User.loginTimeStamp)
                                .toLocaleString();
                            // get userChannelTimeStampInfo
                            $scope
                                .getUserChannelTimeStampInfo($scope.User.userid);
                            $scope.clearLoginInfo();
                            $scope.mainPage.status = true;
                        },
                        function errorCallback(response) {
                            $scope.errorMessage = response.data.responseMessage.message;
                            $scope.clearLoginInfo();
                            if ($scope.errorMessage == "NO_RESULTS_FOUND")
                                $scope.loginFailedPage.status = true;
                            else
                                $scope.userExistedPage.status = true;
                        });
            };
            // socket listeners
            socket
                .on(
                    'send:Message',
                    function(data) {
                        var message = data.message;
                        if ($scope.UserChannelTimeStampInfo != null &&
                            $scope.UserChannelTimeStampInfo.get(message.channelid) != null) {
                            if ($scope.CurrentChannel == null ||
                                $scope.CurrentChannel.channelid != message.channelid) {
                                $scope.setUnreadMessageCount(message.channelid, 1, false);
                            }
                            $scope.UserChannelTimeStampInfo
                                .get(message.channelid).messageList
                                .push(message);
                        }
                    })
            $scope.broadcastMessage = function(message) {
                socket.emit('send:Message', {
                    message: message
                })
            }
        })
app.factory('myService', function($http, $q) {
    var deferred = $q.defer();
    var getData = function(userid, channels, userChannelTimeStampInfo) {
        url = ctx + '/api/userChannelTimeStampEntries/updateUserChannelTimeStamp';
        for (var i = 0; i < channels.length; i++) {
            var channelid = channels[i].channelid;
            var userChannelTimeStamp = {
                userid: userid,
                channelid: channelid,
                timeUpdated: new Date(userChannelTimeStampInfo.get(channelid).timeUpdated).getTime()
            }
            $http
                .post(url, angular.toJson(userChannelTimeStamp))
                .then(
                    function successCallback(response) {
                        return true;
                    },
                    function errorCallback(response) {
                        errorMessage = response.data.responseMessage.message;
                    });
        }
        return deferred.promise;
    };
    return {
        getData: getData
    };
});

app.factory('socket', function($rootScope) {
    /* define socket connection ip and port */
    var socket = io.connect("http://localhost:3000");
    return {
        on: function(eventName, callback) {
            socket.on(eventName, function() {
                var args = arguments;
                $rootScope.$apply(function() {
                    callback.apply(socket, args);
                });
            });
        },
        emit: function(eventName, data, callback) {
            socket.emit(eventName, data, function() {
                var args = arguments;
                $rootScope.$apply(function() {
                    if (callback) {
                        callback.apply(socket, args);
                    }
                });
            })
        }
    };
})
app.directive('myRepeatDirective', function() {
  return function(scope, element, attrs) {
    if (scope.$last){
      scope.$emit('LastElem');
    }
    scope.$watch('thing', function(){
    });
  };
})
app.directive('myMainDirective', function() {
  return function(scope, element, attrs) {
    scope.$on('LastElem', function(event){
    	var out = document.getElementById("char_area_scroll");
    	out.scrollTop = out.scrollHeight - out.clientHeight;
    });
  };
});