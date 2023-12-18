const staticUrl = 'http://localhost:9090';
const usersBlogsBycategory = staticUrl + '/user/graph/blogsByCategory';
const usersBlogAccessTypeAndStatus = staticUrl + '/user/graph/blogsByAccessTypeAndStatus';

const ctx1 = document.getElementById("myChart1");
const ctx2 = document.getElementById("myChart2");

console.log("inside custom js of user chart")
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

setTimeout(async () => {
    try {
        const fetchedData = await fetchAPI(usersBlogsBycategory);
        const donutLabelsname = [];
        const donutchartdata= [];
        for(key in fetchedData){
            donutLabelsname.push(key);
            donutchartdata.push(fetchedData[key]);
        }
        const data = {
        labels: donutLabelsname,
        datasets: [{
            label: 'category chart',
            data: donutchartdata,
            backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(255, 159, 64, 0.2)',
            'rgba(255, 205, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(201, 203, 207, 0.2)',
            'rgba(84, 180, 235, 0.2)',
            'rgba(173, 122, 255, 0.2)',
            'rgba(200, 130, 207, 0.2)'
            ],
            borderColor: [
            'rgb(255, 99, 132)',
            'rgb(255, 159, 64)',
            'rgb(255, 205, 86)',
            'rgb(75, 192, 192)',
            'rgb(54, 162, 235)',
            'rgb(153, 102, 255)',
            'rgb(201, 203, 207)',
            'rgb(54, 162, 235)',
            'rgb(153, 102, 255)',
            'rgb(201, 203, 207)'
            ],
            borderWidth: 1
        }]
        };
        const config = {
            type: 'pie',
            data: data,
            options: {
              indexAxis: 'y', // <-- here
              responsive: true
            },
          };
      new Chart(ctx1, config);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
}, 1000);


setTimeout(async () => {
    try {
        const fetchedData = await fetchAPI(usersBlogAccessTypeAndStatus);
        const donutLabelsname = [];
        const donutchartdata= [];
        for(key in fetchedData){
            donutLabelsname.push(key);
            donutchartdata.push(fetchedData[key]);
        }
        const data = {
        labels: donutLabelsname,
        datasets: [{
            label: 'category chart',
            data: donutchartdata,
            backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(255, 159, 64, 0.2)',
            'rgba(255, 205, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(201, 203, 207, 0.2)',
            'rgba(84, 180, 235, 0.2)',
            'rgba(173, 122, 255, 0.2)',
            'rgba(200, 130, 207, 0.2)'
            ],
            borderColor: [
            'rgb(255, 99, 132)',
            'rgb(255, 159, 64)',
            'rgb(255, 205, 86)',
            'rgb(75, 192, 192)',
            'rgb(54, 162, 235)',
            'rgb(153, 102, 255)',
            'rgb(201, 203, 207)',
            'rgb(54, 162, 235)',
            'rgb(153, 102, 255)',
            'rgb(201, 203, 207)'
            ],
            borderWidth: 1
        }]
        };
        const config = {
            type: 'bar',
            data: data,
            options: {
              indexAxis: 'y', // <-- here
              responsive: true
            },
          };
      new Chart(ctx2, config);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
}, 1000);
