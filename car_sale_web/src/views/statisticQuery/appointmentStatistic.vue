<template>
    <div id="appointmentStatistic-container">
        <el-card class="box-card common">
            <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
            <div id="pie" style="width: 1000px; height:500px; margin: 31px auto;"></div>
        </el-card>
    </div>
</template>
  
<script>
import appointmentApi from '@/api/appointmentRecordManage'
import * as echarts from 'echarts';
export default {
    name: 'AppointmentStatistic',
    data() {
        return {
        }
    },
    methods: {
        getAppointmentPie() {
            appointmentApi.getAppointmentPie().then(res => {
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('pie'));
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption({
                    backgroundColor: '#2c343c',
                    title: {
                        text: '预约记录统计饼图',
                        left: 'center',
                        top: 20,
                        textStyle: {
                            color: '#ccc'
                        }
                    },
                    tooltip: {
                        trigger: 'item'
                    },
                    visualMap: {
                        show: false,
                        min: 80,
                        max: 600,
                        inRange: {
                            colorLightness: [0, 1]
                        }
                    },
                    series: [
                        {
                            name: '预约数',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '50%'],
                            data: res.data.sort(function (a, b) {
                                return a.value - b.value;
                            }),
                            roseType: 'radius',
                            label: {
                                color: 'rgba(255, 255, 255, 0.3)'
                            },
                            labelLine: {
                                lineStyle: {
                                    color: 'rgba(255, 255, 255, 0.3)'
                                },
                                smooth: 0.2,
                                length: 10,
                                length2: 20
                            },
                            itemStyle: {
                                color: '#c23531',
                                shadowBlur: 200,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            },
                            animationType: 'scale',
                            animationEasing: 'elasticOut',
                            animationDelay: function (idx) {
                                return Math.random() * 200;
                            }
                        }
                    ]
                });
            })
        }
    },
    mounted() {
        this.getAppointmentPie();
    },
    activated() {
        this.getAppointmentPie();
    }
}
</script>