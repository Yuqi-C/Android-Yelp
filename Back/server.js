
`use strict`;
const express = require("express");
const axios = require('axios')
const cors = require('cors')
const app = express();
app.use(cors({
    origin: '*'
}))
const port = process.env.PORT || 8080;

const token = "w3N0hcpHPaR0qx4IEJtbXF5QGJ6-9zDWxZmVgHdTvHeWQRGVixoK6Q7X3BvaluP6nQ62pqx-fbvyiTZ9vVX-6R_DocAcFprTOifPuga0syoInNRU-iQH9sqbOHE2Y3Yx"

class YelpApi {
    constructor(bearerToken) {
        this.bearerToken = bearerToken;
    }
    async autoComplete(text) {
        const uri = `https://api.yelp.com/v3/autocomplete?text=${text}`;
        const raw = await axios.get(uri, {headers: {'Authorization': 'Bearer ' + this.bearerToken}})
        const arr = [];
        for(const item of raw.data.categories){
            arr.push(item.title);
        }
        for(const item of raw.data.terms){
            arr.push(item.text);
        }
        return arr;
    }

    async getBusinesses(q) {
        const uri = `https://api.yelp.com/v3/businesses/search?term=${q.keyword}&latitude=${parseFloat(q.lat)}&longitude=${parseFloat(q.lgt)}&categories=${q.category}&radius=${Math.floor(parseFloat(q.distance)*1609.34)}`;
        const raw = await axios.get(uri, {headers: {'Authorization': 'Bearer ' + this.bearerToken}})
        const businesses = raw.data.businesses;
        const arr = [];
        for(const business of businesses){
            arr.push(business);
        }
        return arr;
    }

    async getBusiness(id) {
        const uri = `https://api.yelp.com/v3/businesses/${id}`;
        const business = await axios.get(uri, {headers: {'Authorization': 'Bearer ' + this.bearerToken}})
        return business.data;
    }

    async getReviews(id) {
        const uri = `https://api.yelp.com/v3/businesses/${id}/reviews`;
        const reviews = await axios.get(uri, {headers: {'Authorization': 'Bearer ' + this.bearerToken}})
        return reviews.data;
    }

} 
const source = new YelpApi(token);
app.use(express.json());
app.use(express.urlencoded({ extended: true}));

app.get('/search', async (req, res) => {
    const businesses = await source.getBusinesses(req.query);
    res.json(businesses);
})

app.get('/searchId/:pid', async (req, res) => { 
    const business = await source.getBusiness(req.params.pid); 
    res.json(business);
})

app.get('/reviews/:pid', async (req, res) => {
    const reviews = await source.getReviews(req.params.pid);
    res.json(reviews);
})

app.get('/autocomplete', async (req, res) => {
    const content = await source.autoComplete(req.query.text);
    res.json(content);
})

app.listen(port)