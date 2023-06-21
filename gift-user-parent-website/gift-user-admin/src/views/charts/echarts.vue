<template>
    <section class="chart-container">
        <el-row>
            <el-col :span="12">
                <div id="chartColumn" style="width:100%; height:400px;"></div>
            </el-col>
            <el-col :span="12">
                <div id="chartBar" style="width:100%; height:400px;"></div>
            </el-col>
            <el-col :span="12">
                <div id="chartLine" style="width:770px; height:400px;"></div>
            </el-col>
            <el-col :span="12">
                <div id="chartPie" style="width:100%; height:400px;text-align: right"></div>
            </el-col>
            <el-col :span="24">
                <a href="http://echarts.baidu.com/examples.html" target="_blank" style="float: right;">more>></a>
            </el-col>
        </el-row>
    </section>
</template>

<script>
    import echarts from 'echarts'

    export default {
        data() {
            return {
                chartColumn: null,
                chartBar: null,
                chartLine: null,
                chartPie: null,
                allSource:[],
                allLine:[]
            }
        },

        methods: {
          getAllSource(){
            this.$http.get("/deposit/line")
                .then(result=>{
                  result = result.data;
                  if(result.success){
                    this.allLine = result.resultObject;
                  }

                  console.log(this.allSource[0].name)
                })
          },
          getAllLine(){
            this.$http.get("/dictionaryItem/source")
                .then(result=>{
                  result = result.data;
                  if(result.success){
                    this.allSource = result.resultObject;
                  }

                })
          },

            drawColumnChart() {
                this.chartColumn = echarts.init(document.getElementById('chartColumn'));
                this.chartColumn.setOption({
                  title: { text: 'Column Chart' },
                  tooltip: {},
                  xAxis: {
                      data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
                  },
                  yAxis: {},
                  series: [{
                      name: '销量',
                      type: 'bar',
                      data: [5, 20, 36, 10, 10, 20]
                    }]
                });
            },
            drawBarChart() {
                this.chartBar = echarts.init(document.getElementById('chartBar'));
                this.chartBar.setOption({
                    title: {
                        text: 'Bar Chart',
                        subtext: '数据来自网络'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        data: ['2011年', '2012年']
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value',
                        boundaryGap: [0, 0.01]
                    },
                    yAxis: {
                        type: 'category',
                        data: ['巴西', '印尼', '美国', '印度', '中国', '世界人口(万)']
                    },
                    series: [
                        {
                            name: '2011年',
                            type: 'bar',
                            data: [18203, 23489, 29034, 104970, 131744, 630230]
                        },
                        {
                            name: '2012年',
                            type: 'bar',
                            data: [19325, 23438, 31000, 121594, 134141, 681807]
                        }
                    ]
                });
            },
            drawLineChart() {
                this.chartLine = echarts.init(document.getElementById('chartLine'));
               console.log(this.allSource)
                this.chartLine.setOption({
                    title: {
                        text: 'Line Chart'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['销售总额/万','销售数量/千']
                    },
                    grid: {
                        left: '2%',
                        right: '8%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        //data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
                      data:[this.allLine[0].date,this.allLine[1].date,this.allLine[2].date,
                            this.allLine[3].date,this.allLine[4].date,this.allLine[5].date,
                            this.allLine[6].date]
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [
                        {
                            name: '销售总额/万',
                            type: 'line',
                            stack: '总量',
                            data: [this.allLine[0].salesSum/100,this.allLine[1].salesSum/100,this.allLine[2].salesSum/100,
                                   this.allLine[3].salesSum/100,this.allLine[4].salesSum/100,this.allLine[5].salesSum/100,
                                   this.allLine[6].salesSum/100]
                        },
                        {
                            name: '销售数量/千',
                          type: 'line',
                          stack: '总量',
                          data: [this.allLine[0].salesCount,this.allLine[1].salesCount,this.allLine[2].salesCount,
                                  this.allLine[3].salesCount,this.allLine[4].salesCount,this.allLine[5].salesCount,
                                  this.allLine[6].salesCount]
                        },

                    ]
                });
            },
            drawPieChart() {
                console.log(this.allSource)
                this.chartPie = echarts.init(document.getElementById('chartPie'));
                this.chartPie.setOption({
                    title: {
                        text: 'Pie Chart',
                        subtext: '纯属虚构',
                        x: 'right'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'center',
                        data: [this.allSource[0].name,this.allSource[1].name,this.allSource[2].name,this.allSource[3].name]
                    },
                    series: [
                        {
                            name: '访问来源',
                            type: 'pie',
                            radius: '45%',
                            center: ['70%', '60%'],

                            //data:this.allSource,
                            data: [
                                { value: this.allSource[0].value, name: this.allSource[0].name },
                                { value: this.allSource[1].value, name: this.allSource[1].name },
                                { value: this.allSource[2].value, name: this.allSource[2].name },
                                { value: this.allSource[3].value, name: this.allSource[3].name },

                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(2, 5, 6, 0.5)'
                                }
                            }
                        }
                    ]
                });
            },
            drawCharts() {
                this.drawColumnChart()
                this.drawBarChart()
                this.drawLineChart()
                this.drawPieChart()
            },

        },

      mounted: function () {
        setTimeout(() => {
          this.drawCharts();
        }, 1000)
        this.getAllSource();
        this.getAllLine();
      },
        updated: function () {
            this.drawCharts()
        },

    }
</script>

<style scoped>
    .chart-container {
        width: 100%;
        float: left;
    }
    /*.chart div {
        height: 400px;
        float: left;
    }*/

    .el-col {
        padding: 30px 20px;
    }
</style>
