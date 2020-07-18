//存放主要交互逻辑js，javascript 模块化
var seckill = {
    // 封装秒杀相关ajax的url
    URL : {
        now : function() {
            return '/seckill/time/now';
        },
        exposer : function(seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        excution : function(seckillId,md5){
            return '/seckill/'+seckillId+'/'+md5+'/execution';
        }
    },
    // 3、秒杀开启后，处理秒杀
    handleSeckill : function(seckillId, node) {
        // 处理秒杀逻辑
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');// 按钮
        $.post(seckill.URL.exposer(seckillId), {}, function(result) {
            // 判断是否在秒杀时间内，返回SeckillResult<Exposer>
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    // 开启秒杀，获取秒杀地址
                    var md5=exposer['md5'];
                    var killUrl=seckill.URL.excution(seckillId,md5);
                    console.log("killUrl:"+killUrl);
                    //绑定一次点击事件
                    $('#killBtn').one('click',function(){
                        //禁用按钮
                        $(this).addClass('disabled');
                        //发送秒杀请求,返回SeckillResult<Execution>
                        $.post(killUrl,{},function(result){
                            if (result && result['success']) {
                                var killResult=result['data'];
                                var stateInfo=killResult['stateInfo'];
                                // console.log("stateInfo:"+stateInfo);
                                node.html('<span class="label label-success">'+stateInfo+'</span>')
                            }
                        });
                    });
                    node.show();
                } else {
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重写计算计时逻辑
                    seckill.countdown(seckillId, now, start, end);
                }
            } else {
                console.log('result' + result);
            }
        });
    },
    // 2、秒杀倒计时
    countdown : function(seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        // 时间判断
        if (nowTime > endTime) {
            // 秒杀结束，按钮显示秒杀结束
            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            // 秒杀未开始，计时
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function(event) {
                var format = event.strftime('秒杀倒计时： %D天   %H时  %M分  %S秒');
                seckillBox.html(format);
                /* 时间完成后回调事件 */
            }).on('finish', function() {
                // 获取秒杀地址，控制显示逻辑，执行秒杀
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {
            // 秒杀开始
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },
    // 1.1、验证手机号
    validatePhone : function(phone) {
        return !!(phone && phone.length == 11 && !isNaN(phone));
    },
    // 1、详情页秒杀逻辑
    detail : {
        // 详情页初始化，判断是否登陆，未登陆则跳出登陆框，验证通过把电话放入cookie中
        init : function(params) {
            // 手机验证和登录,计时交互
            // 规划交互流程
            var killPhone = $.cookie('killPhone');
            // 验证手机号
            if (!seckill.validatePhone(killPhone)) {
                // 绑定手机 控制输出
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show : true, // 显示弹出层
                    backdrop : false, // 禁止位置关闭
                    keyboard : false // 关闭键盘事件
                });
                $('#killPhoneBtn').click(function() {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        $.cookie('killPhone', inputPhone, {expires : 1, path : '/seckill'});
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });
            }
            // 登录成功后，计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(), {}, function(result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    // 时间判断,计时交互逻辑
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result' + result);
                }
            });
        }
    }
};