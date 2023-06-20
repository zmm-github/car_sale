<template>
    <div id="rescueStatistic-container">
        <el-card class="box-card common">
            <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
            <div id="line" style="width: 1000px; height:500px; margin: 31px auto;"></div>
        </el-card>
    </div>
</template>
  
<script>
import rescueApi from '@/api/rescueRecordManage'
import * as echarts from 'echarts';
export default {
    name: 'RescueStatistic',
    data() {
        return {
        }
    },
    methods: {
        getRescueLine() {
            rescueApi.getRescueLine().then(res => {
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('line'));
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption({
                    title: {
                        text: '救援统计线性图',
                        left: 'center',
                        top: 20,
                        textStyle: {
                            color: '#555555'
                        }
                    },
                    xAxis: {
                        name: '年月',
                        type: 'category',
                        data: res.data.dateList
                    },
                    yAxis: {
                        name: '救援数',
                        type: 'value'
                    },
                    series: [
                        {
                            name: '救援数',
                            data: res.data.values,
                            type: 'line'
                        }
                    ]
                });
            })
        }
    },
    mounted() {
        this.getRescueLine();
    },
    activated() {
        this.getRescueLine();
    }
}
</script>