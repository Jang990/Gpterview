function createUserMessage(msg) {
    return `
           <li class="mb-3 d-flex justify-content-end">
                <div class="d-flex flex-row justify-content-end col-8" style="min-width:0px;">
                    <div class="p-3 me-3 border" style="border-radius: 15px; background-color: #fbfbfb; min-width:0px;">
                        <input type="hidden" name="role" class="role" value="USER">
                        <p class="small mb-0 content" name="content">${msg}</p>
                    </div>
                    <img src="../image/applicant.png" alt="avatar"
                     class="rounded-circle d-flex align-self-start shadow-1-strong"
                     style="width: 50px; height: 50px;">
                </div>
           </li>`;
}