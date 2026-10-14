document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("searchForm");
    const message = document.getElementById("message");

    if (!form || !message) {
        console.error("Booking form or message element not found.");
        return;
    }

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        const source = document.getElementById("source").value.trim();
        const destination = document.getElementById("destination").value.trim();
        const date = document.getElementById("date").value;
        const travelClass = document.getElementById("travelClass").value;

        if (!source || !destination || !date) {
            message.textContent = "Please fill in all journey details.";
            return;
        }

        if (source.toLowerCase() === destination.toLowerCase()) {
            message.textContent = "Source and destination must be different.";
            return;
        }

        message.textContent = "Connecting to Java backend...";

        try {
            const response = await fetch("/api/journey/search", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    from: source,
                    to: destination,
                    date: date,
                    travelClass: travelClass
                })
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || "Server error");
            }

            message.textContent =
                result.message +
                (result.pnr ? " Demo PNR: " + result.pnr : "");

        } catch (error) {
            message.textContent =
                "Error: " + error.message +
                ". Please check the Java backend.";
            console.error("Journey search failed:", error);
        }
    });
});