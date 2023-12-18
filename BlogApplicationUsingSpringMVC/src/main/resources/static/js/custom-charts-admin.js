

const staticUrl = 'http://localhost:9090';
const activeAndInactiveUserURL = staticUrl + "/admin/graph/activeUsers";
const categoryWiseBlogCount = staticUrl + "/admin/graph/blogsCategoryWise";
const usersRegesteredMonthWise = staticUrl + "/admin/graph/userRegisteredMonthWise";
const blogBasesOnAccessTypeAndStatus = staticUrl + "/admin/graph/blogRadarGraphValues";

async function fetchAPI(url){
  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch(error) {
      throw error;
  }
}

const ctx = document.getElementById('myChart');
const ctx2 = document.getElementById('myChart2');
const ctx3 = document.getElementById('myChart3');
const ctx4 = document.getElementById('myChart4');


// Active Users v/s Deactive Users;
setTimeout(async () => {
    try {
      const fetchedData = await fetchAPI(activeAndInactiveUserURL);
      const donutLabelsname = [];
      const donutchartdata= [];
      for(key in fetchedData){
        donutLabelsname.push(key);
        donutchartdata.push(fetchedData[key]);
      }
      // donut graph
      const donutdata = {
        labels:donutLabelsname,
        datasets: [{
          label: 'My First Dataset',
          data: donutchartdata,
          backgroundColor: [
            'rgb(255, 99, 132)',
            'rgb(54, 162, 235)',
            'rgb(255, 205, 86)'
          ],
          hoverOffset: 4
        }]
      };
      const config = {
        type: 'doughnut',
        data: donutdata,
      };
      new Chart(ctx3, config);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
}, 1000);


setTimeout(async () => {
    try {
      const fetchedData = await fetchAPI(categoryWiseBlogCount);
      const donutLabelsname = [];
      const donutchartdata= [];
      for(key in fetchedData){
        donutLabelsname.push(key);
        donutchartdata.push(fetchedData[key]);
      }
      // donut graph
      const donutdata = {
        labels:donutLabelsname,
        datasets: [{
          label: 'My First Dataset',
          data: donutchartdata,
          backgroundColor: [
            'rgb(255, 99, 132)',
            'rgb(54, 162, 235)',
            'rgb(180, 205, 86)',
            'rgb(200, 199, 132)',
            'rgb(20, 62, 235)',
            'rgb(220, 200, 86)',
            'rgb(140, 166, 235)',
            'rgb(255, 183, 86)'
          ],
          hoverOffset: 4
        }]
      };
      const config = {
        type: 'doughnut',
        data: donutdata,
      };
      new Chart(ctx4, config);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
}, 1000);


setTimeout(async () => {
    try {
      const fetchedData = await fetchAPI(usersRegesteredMonthWise);
      const barLabelsname = [];
      const barchartdata= [];
      for(key in fetchedData){
        barLabelsname.push(key);
        barchartdata.push(fetchedData[key]);
      }
      // donut graph
      const barGraph = {
        type: 'bar',
        data: {
          labels: barLabelsname,
          datasets: [{
            label: 'no of users per month',
            data: barchartdata,
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(255, 159, 64, 0.2)',
              'rgba(255, 205, 86, 0.2)',
              'rgba(75, 192, 192, 0.2)',
              'rgba(54, 162, 235, 0.2)',
              'rgba(153, 102, 255, 0.2)',
              'rgba(201, 203, 207, 0.2)',
              'rgba(255, 205, 86, 0.2)',
              'rgba(75, 192, 192, 0.2)',
              'rgba(54, 162, 235, 0.2)',
              'rgba(153, 102, 255, 0.2)',
              'rgba(201, 203, 207, 0.2)',
            ],
            borderColor: [
              'rgb(255, 99, 132)',
              'rgb(255, 159, 64)',
              'rgb(255, 205, 86)',
              'rgb(75, 192, 192)',
              'rgb(54, 162, 235)',
              'rgb(153, 102, 255)',
              'rgb(201, 203, 207)',
              'rgb(255, 205, 86)',
              'rgb(75, 192, 192)',
              'rgb(54, 162, 235)',
              'rgb(153, 102, 255)',
              'rgb(201, 203, 207)',
            ],
            borderWidth: 1
          }]
        },
        options: {
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      };
      new Chart(ctx,  barGraph);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
}, 1000);


setTimeout(async () => {
  try {
    const fetchedData = await fetchAPI(blogBasesOnAccessTypeAndStatus);
    const donutLabelsname = [];
    const donutchartdata= [];
    for(key in fetchedData){
      donutLabelsname.push(key);
      donutchartdata.push(fetchedData[key]);
    }
    const data = {
      labels:donutLabelsname,
      datasets: [{
        label: 'My First Dataset',
        data: donutchartdata,
        backgroundColor: [
          'rgb(255, 99, 132)',
          'rgb(75, 192, 192)',
          'rgb(255, 205, 86)',
          'rgb(201, 203, 207)',
          'rgb(54, 162, 235)'
        ]
      }]
    };
    const config = {
      type: 'polarArea',
      data: data,
      options: {}
    };
    new Chart(ctx2, config);
  } catch (error) {
    console.error('Error fetching data:', error);
  }
}, 1000);




