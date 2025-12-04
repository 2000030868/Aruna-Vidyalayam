document.getElementById("registerForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const student = {
        name: document.getElementById("name").value,
        rollNumber: document.getElementById("rollNumber").value,
        classroomId: document.getElementById("classroomId").value
    };

    try {
        const response = await fetch("http://localhost:8080/api/students", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(student)
        });

        const messageElement = document.getElementById("message");

        if (response.ok) {
            messageElement.textContent = "✅ Student registered successfully!";
            messageElement.style.color = "green";
            document.getElementById("registerForm").reset();
        } else {
            const errorData = await response.json();
            messageElement.textContent = "❌ Failed: " + (errorData.message || "Server error");
            messageElement.style.color = "red";
        }
    } catch (error) {
        document.getElementById("message").textContent = "⚠️ Network error!";
    }
});
