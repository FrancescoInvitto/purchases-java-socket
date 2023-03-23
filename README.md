# purchases-java-socket

The goal is to create a client - server application for the purchase of products at an advantageous cost.

This application involves a server and a certain number of clients (at least 3).

The clients and the server will interact through socket connections.

The products have a price that varies within a range between 10 and 200 €.
The server periodically randomly generates sales prices and informs clients of their value.
Each customer receives the prices of the products from time to time, randomly generates the maximum purchase price (always in the range between 10 and 200 €) and sends a purchase request to the server if the sale price is lower of the maximum purchase price.

The server, having received the purchase request, sends a confirmation of sale, if the purchase price is higher or equal to the current selling price; otherwise send a rejection message.

Each client terminates its activity after the same number of purchases (at least 10).

When all clients have completed their purchases, the server terminates the application.

# How to use the application
In order to use the application do the following:
 - start the server
 - start at least 3 clients