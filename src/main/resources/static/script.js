const API = "";


/* =========================
   SECTION NAVIGATION
========================= */

function showSection(sectionId, button) {

    document.querySelectorAll(".section").forEach(section => {
        section.classList.remove("active");
    });

    document.getElementById(sectionId).classList.add("active");

    document.querySelectorAll(".menu-btn").forEach(btn => {
        btn.classList.remove("active");
    });

    if (button) {
        button.classList.add("active");
    }

    if (sectionId === "dashboard") {
        loadDashboard();
    }

    if (sectionId === "members") {
        loadMembers();
    }

    if (sectionId === "plans") {
        loadPlans();
    }
}


/* =========================
   LOAD PLANS
========================= */

async function loadPlans() {

    try {

        const response = await fetch(API + "/api/plans");

        if (!response.ok) {
            throw new Error("Failed to load plans");
        }

        const plans = await response.json();

        const memberPlan =
            document.getElementById("memberPlan");

        const renewPlan =
            document.getElementById("renewPlan");

        const plansList =
            document.getElementById("plansList");

        memberPlan.innerHTML =
            '<option value="">Select Plan</option>';

        renewPlan.innerHTML =
            '<option value="">Select Plan</option>';

        plansList.innerHTML = "";

        plans.forEach(plan => {

            const option1 = document.createElement("option");

            option1.value = plan.id;

            option1.textContent =
                `${plan.name} - ₹${plan.price}`;

            memberPlan.appendChild(option1);


            const option2 = document.createElement("option");

            option2.value = plan.id;

            option2.textContent =
                `${plan.name} - ₹${plan.price}`;

            renewPlan.appendChild(option2);


            const div = document.createElement("div");

            div.className = "list-item";

            div.innerHTML = `
                <h3>${plan.name}</h3>
                <p>Duration: ${plan.duration}</p>
                <p>Price: ₹${plan.price}</p>
                <p>Plan ID: ${plan.id}</p>
            `;

            plansList.appendChild(div);
        });

        document.getElementById("totalPlans").textContent =
            plans.length;

    } catch (error) {

        console.error(error);

    }
}


/* =========================
   LOAD MEMBERS
========================= */

async function loadMembers() {

    try {

        const response =
            await fetch(API + "/api/members");

        if (!response.ok) {
            throw new Error("Failed to load members");
        }

        const members =
            await response.json();

        const membersList =
            document.getElementById("membersList");

        membersList.innerHTML = "";

        members.forEach(member => {

            const div =
                document.createElement("div");

            div.className = "list-item";

            div.innerHTML = `
                <h3>${member.name}</h3>
                <p>Email: ${member.email}</p>
                <p>Phone: ${member.phone}</p>
                <p>Member ID: ${member.id}</p>
            `;

            membersList.appendChild(div);
        });

        document.getElementById("totalMembers").textContent =
            members.length;

    } catch (error) {

        console.error(error);

    }
}


/* =========================
   DASHBOARD
========================= */

async function loadDashboard() {

    await loadPlans();

    await loadMembers();

    try {

        const response =
            await fetch(API + "/api/memberships/expiring");

        const memberships =
            await response.json();

        document.getElementById("expiringCount").textContent =
            memberships.length;

    } catch (error) {

        console.error(error);

    }
}


/* =========================
   REGISTER MEMBER
========================= */

document.getElementById("memberForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const data = {

            name:
                document.getElementById("memberName").value,

            email:
                document.getElementById("memberEmail").value,

            phone:
                document.getElementById("memberPhone").value,

            planId:
                Number(document.getElementById("memberPlan").value)
        };


        try {

            const response =
                await fetch(API + "/api/members", {

                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(data)
                });


            const result =
                await response.json();


            if (!response.ok) {

                throw new Error(
                    result.error ||
                    "Failed to register member"
                );
            }


            document.getElementById("memberMessage").textContent =
                "Member registered successfully!";

            document.getElementById("memberMessage")
                .className = "message success";


            document.getElementById("memberForm").reset();

            loadMembers();

            loadDashboard();

        } catch (error) {

            document.getElementById("memberMessage")
                .textContent = error.message;

            document.getElementById("memberMessage")
                .className = "message error";
        }

    });


/* =========================
   CREATE PLAN
========================= */

document.getElementById("planForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const data = {

            name:
                document.getElementById("planName").value,

            duration:
                document.getElementById("planDuration").value,

            price:
                Number(document.getElementById("planPrice").value)
        };


        try {

            const response =
                await fetch(API + "/api/plans", {

                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(data)
                });


            const result =
                await response.json();


            if (!response.ok) {

                throw new Error(
                    result.error ||
                    "Failed to create plan"
                );
            }


            document.getElementById("planMessage")
                .textContent =
                "Plan created successfully!";

            document.getElementById("planMessage")
                .className = "message success";


            document.getElementById("planForm").reset();

            loadPlans();

        } catch (error) {

            document.getElementById("planMessage")
                .textContent = error.message;

            document.getElementById("planMessage")
                .className = "message error";
        }

    });


/* =========================
   CHECK-IN
========================= */

document.getElementById("checkinForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const data = {

            membershipId:
                Number(
                    document.getElementById(
                        "checkinMembershipId"
                    ).value
                )
        };


        try {

            const response =
                await fetch(
                    API + "/api/memberships/check-in",
                    {

                        method: "POST",

                        headers: {
                            "Content-Type": "application/json"
                        },

                        body: JSON.stringify(data)
                    }
                );


            const result =
                await response.json();


            if (!response.ok) {

                throw new Error(
                    result.error ||
                    "Check-in failed"
                );
            }


            document.getElementById("checkinMessage")
                .textContent =
                "Check-in successful!";

            document.getElementById("checkinMessage")
                .className = "message success";


            document.getElementById("checkinForm").reset();

        } catch (error) {

            document.getElementById("checkinMessage")
                .textContent = error.message;

            document.getElementById("checkinMessage")
                .className = "message error";
        }

    });


/* =========================
   RENEW MEMBERSHIP
========================= */

document.getElementById("renewForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const membershipId =
            document.getElementById(
                "renewMembershipId"
            ).value;

        const data = {

            planId:
                Number(
                    document.getElementById(
                        "renewPlan"
                    ).value
                )
        };


        try {

            const response =
                await fetch(
                    API +
                    "/api/memberships/" +
                    membershipId +
                    "/renew",
                    {

                        method: "PUT",

                        headers: {
                            "Content-Type": "application/json"
                        },

                        body: JSON.stringify(data)
                    }
                );


            const result =
                await response.json();


            if (!response.ok) {

                throw new Error(
                    result.error ||
                    "Renewal failed"
                );
            }


            document.getElementById("renewMessage")
                .textContent =
                `Membership renewed successfully. New end date: ${result.endDate}`;

            document.getElementById("renewMessage")
                .className = "message success";


            document.getElementById("renewForm").reset();

        } catch (error) {

            document.getElementById("renewMessage")
                .textContent = error.message;

            document.getElementById("renewMessage")
                .className = "message error";
        }

    });


/* =========================
   ATTENDANCE
========================= */

document.getElementById("attendanceForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const memberId =
            document.getElementById(
                "attendanceMemberId"
            ).value;


        try {

            const response =
                await fetch(
                    API +
                    "/api/memberships/attendance/" +
                    memberId
                );


            const count =
                await response.json();


            if (!response.ok) {
                throw new Error("Unable to get attendance");
            }


            document.getElementById(
                "attendanceResult"
            ).innerHTML = `
                <strong>Current Month Attendance:</strong>
                ${count} day(s)
            `;

        } catch (error) {

            document.getElementById(
                "attendanceResult"
            ).textContent = error.message;
        }

    });


/* =========================
   INITIAL LOAD
========================= */

loadDashboard();