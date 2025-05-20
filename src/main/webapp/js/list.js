function filterFeedbacks() {
    const input = document.getElementById("searchInput").value.toLowerCase();
    const rows = document.querySelectorAll("tbody tr");
    rows.forEach(row => {
        const nameCell = row.querySelector("td:nth-child(1)");
        const name = nameCell ? nameCell.textContent.toLowerCase() : "";
        row.style.display = name.includes(input) ? "" : "none";
    });
}

function confirmDelete(index, name) {
    if (confirm("Are you sure you want to delete feedback from " + name + "?")) {
        document.getElementById("deleteForm" + index).submit();
    }
}

function closeNotification() {
    const successBox = document.getElementById('successBox');
    if (successBox) {
        successBox.style.opacity = '0';
        successBox.style.transform = 'translateY(-10px)';
        setTimeout(() => {
            successBox.style.display = 'none';
        }, 300);
    }
}

// Auto-hide success message after 5 seconds
window.addEventListener("DOMContentLoaded", function () {
    setTimeout(() => {
        closeNotification();
    }, 5000);
});
