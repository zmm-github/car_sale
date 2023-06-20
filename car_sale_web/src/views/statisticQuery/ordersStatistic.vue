<template>
    <div id="ordersStatistic-container">
        <el-card class="box-card common">
            <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
            <div id="bar" style="width: 1000px; height:500px; margin: 31px auto;"></div>
        </el-card>
    </div>
</template>
  
<script>
import ordersApi from '@/api/ordersManage'
import * as echarts from 'echarts';
export default {
    name: 'OrdersStatistic',
    data() {
        return {
        }
    },
    methods: {
        getOrdersBar() {
            ordersApi.getOrdersBar().then(res => {
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('bar'));
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption({
                    title: {
                        text: '订单统计柱状图',
                        left: 'center',
                        top: 20,
                        textStyle: {
                            color: '#555555'
                        }
                    },
                    tooltip: {},
                    // legend: {
                    //     data: ['订单']
                    // },
                    xAxis: {
                        data: res.data.names
                    },
                    yAxis: {},
                    series: [
                        {
                            name: '订单',
                            type: 'bar',
                            data: res.data.values
                        }
                    ]
                });
            })
        }
    },
    mounted() {
        this.getOrdersBar();
    },
    activated() {
        this.getOrdersBar();
    }
}
</script>